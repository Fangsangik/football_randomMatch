package com.side.football_project.domain.match.service;

import com.side.football_project.domain.notification.service.MatchNotificationService;
import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.repository.ReservationRepository;
import com.side.football_project.domain.reservation.service.UserReservationService;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserTier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomMatchService implements RandomService {

    private final UserReservationService reservationService;
    private final Map<String, Queue<User>> matchQueue = new ConcurrentHashMap<>();
    private final List<UserTier> tiers = List.of(UserTier.ROOKIE, UserTier.BEGINNER, UserTier.AMATEUR, UserTier.SEMI_PRO, UserTier.PRO);
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final StadiumService stadiumService;
    private final MatchNotificationService matchNotificationService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void joinMatch(Long userId) {
        User user = userService.findUserById(userId);
        matchQueue.computeIfAbsent(user.getTier().name(), key -> new LinkedList<>()).add(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void fillEmptySpot(Long stadiumId) {
        Stadium stadium = stadiumService.findByIdWithLock(stadiumId);

        int current = stadium.getCurrentReservationCount();
        int capacity = stadium.getCapacity();

        UserTier avgTier = averageTier(stadium.getId());

        while (current < capacity) {
            User user = findRandomUserByTier(avgTier);

            if (user == null) {
                log.info("매칭 가능한 사용자가 없습니다. 구독자들에게 알림을 전송합니다.");

                // 빈 자리 알림 전송
                int availableSpots = capacity - current;
                StadiumResponseDto stadiumDto = StadiumResponseDto.toEntity(stadium);
                matchNotificationService.notifyAvailableMatch(stadiumDto, avgTier, availableSpots);
                break;
            }

            ReservationRequestDto requestDto = ReservationRequestDto.builder()
                    .userId(user.getId())
                    .name("Random Match")
                    .stadiumId(stadium.getId())
                    .build();

            reservationService.createReservation(requestDto, user);
            current++;
            log.info("사용자 [{}]가 경기장 [{}]에 매칭되었습니다.", user.getId(), stadium.getId());
            
            // 매칭 완료 SMS 알림 전송
            sendMatchNotificationToUser(user, stadium);
        }
    }

    /**
     * 주어진 티어를 기준으로 매칭 대기열에서 사용자를 꺼내는 메서드
     * - 우선 해당 티어에서 사용자를 찾고,
     * - 없으면 주변 티어(상하 티어)를 탐색하여 매칭 가능한 사용자를 찾습니다
     * @param tier
     * @return
     */
    private User findRandomUserByTier(UserTier tier) {
        Queue<User> queue = matchQueue.getOrDefault(tier.name(), new LinkedList<>());

        if (!queue.isEmpty()) {
            return queue.poll();
        }

        int index = tiers.indexOf(tier);
        for (int offset = 1; offset < tiers.size(); offset++) {
            int lower = index - offset;
            int upper = index + offset;

            // lower 티어에서 사용자 찾기
            if (lower >= 0) {
                Queue<User> lowerQueue = matchQueue.getOrDefault(tiers.get(lower).name(), new LinkedList<>());
                if (!lowerQueue.isEmpty()) {
                    return lowerQueue.poll();
                }
            }

            // upper 티어에서 사용자 찾기
            if (upper < tiers.size()) {
                Queue<User> upperQueue = matchQueue.getOrDefault(tiers.get(upper).name(), new LinkedList<>());

                if (!upperQueue.isEmpty()) {
                    return upperQueue.poll();
                }
            }
        }

        return null;
    }

    /**
     * 매칭이 완료된 사용자에게 SMS 알림을 전송합니다.
     * @param user 매칭된 사용자
     * @param stadium 매칭된 경기장
     */
    private void sendMatchNotificationToUser(User user, Stadium stadium) {
        String message = String.format(
                "[축구매치] %s님이 %s 경기장 매칭이 완료되었습니다! 경기를 즐기세요. ⚽",
                user.getName(),
                stadium.getName()
        );
        
        try {
            StadiumResponseDto stadiumDto = StadiumResponseDto.toEntity(stadium);
            matchNotificationService.sendNotificationToSubscribers(stadiumDto, user.getTier(), message);
            log.info("매칭 완료 알림 전송: User {}, Stadium {}", user.getId(), stadium.getId());
        } catch (Exception e) {
            log.error("매칭 완료 알림 전송 실패: User {}, Stadium {}, Error: {}", 
                     user.getId(), stadium.getId(), e.getMessage());
        }
    }

    private UserTier averageTier(Long stadiumId) {
        List<Reservation> reservations = reservationRepository.findAllByStadiumId(stadiumId);

        List<UserTier> userTiers = reservations
                .stream()
                .map(reservation -> reservation.getUser().getTier())
                .toList();

        if (userTiers.isEmpty()) {
            return UserTier.ROOKIE;
        }

        int sum = userTiers.stream().mapToInt(tiers::indexOf).sum();
        int avgIndex = sum / userTiers.size();

        return tiers.get(Math.min(avgIndex, tiers.size() - 1));
    }
}
