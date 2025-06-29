package com.side.football_project.domain.notification.service;

import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionRequestDto;
import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionResponseDto;
import com.side.football_project.domain.notification.entity.MatchNotificationSubscription;
import com.side.football_project.domain.notification.repository.MatchNotificationSubscriptionRepository;
import com.side.football_project.domain.sms.SmsRequestDto;
import com.side.football_project.domain.sms.SmsUtil;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.repository.StadiumRepository;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.repository.UserRepository;
import com.side.football_project.domain.user.type.UserTier;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.VendorErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchNotificationServiceImpl implements MatchNotificationService {

    private final MatchNotificationSubscriptionRepository subscriptionRepository;
    private final StadiumRepository stadiumRepository;
    private final UserRepository userRepository;
    private final SmsUtil smsUtil;

    /**
     * 매 5분마다 빈 경기장이 있는지 확인하고 구독자들에게 알림을 전송합니다.
     */
    @Scheduled(fixedRate = 300000) // 5분마다 실행 (300,000ms = 5분)
    @Transactional(readOnly = true)
    public void checkAndNotifyAvailableMatches() {
        try {
            log.info("스케줄된 매치 알림 확인 작업 시작");
            
            // 현재 활성화된 경기장들 중에서 빈 자리가 있는 경기장들을 찾습니다
            List<Stadium> availableStadiums = stadiumRepository.findAvailableStadiumsWithSpots();
            
            if (availableStadiums.isEmpty()) {
                log.debug("빈 자리가 있는 경기장이 없습니다.");
                return;
            }
            
            for (Stadium stadium : availableStadiums) {
                int availableSpots = stadium.getCapacity() - stadium.getCurrentReservationCount();
                if (availableSpots > 0) {
                    // 해당 경기장의 평균 티어 계산 (별도 메서드 필요)
                    UserTier averageTier = calculateAverageTier(stadium);
                    
                    // 알림 전송
                    StadiumResponseDto stadiumDto = StadiumResponseDto.toEntity(stadium);
                    notifyAvailableMatch(stadiumDto, averageTier, availableSpots);
                }
            }
            
            log.info("스케줄된 매치 알림 확인 작업 완료: {} 개 경기장 처리", availableStadiums.size());
            
        } catch (Exception e) {
            log.error("스케줄된 매치 알림 확인 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 매주 월요일 오전 9시에 주간 매치 현황을 구독자들에게 전송합니다.
     */
    @Scheduled(cron = "0 0 9 * * MON") // 매주 월요일 오전 9시
    @Transactional(readOnly = true)
    public void sendWeeklyMatchSummary() {
        try {
            log.info("주간 매치 현황 알림 작업 시작");
            
            List<MatchNotificationSubscription> activeSubscriptions = 
                subscriptionRepository.findByIsActiveTrue();
            
            if (activeSubscriptions.isEmpty()) {
                log.info("활성화된 구독자가 없습니다.");
                return;
            }
            
            String weeklyMessage = generateWeeklyMatchSummary();
            
            List<SmsRequestDto> smsRequests = activeSubscriptions.stream()
                    .map(sub -> new SmsRequestDto(sub.getPhoneNumber(), weeklyMessage))
                    .collect(Collectors.toList());
            
            smsUtil.sendBulkAndLog(smsRequests);
            log.info("주간 매치 현황 알림 전송 완료: {} 명에게 전송", activeSubscriptions.size());
            
        } catch (Exception e) {
            log.error("주간 매치 현황 알림 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 경기장의 평균 티어를 계산합니다.
     */
    private UserTier calculateAverageTier(Stadium stadium) {
        // 현재 예약된 사용자들의 평균 티어를 계산하는 로직
        // 기본값으로 AMATEUR 반환 (실제 구현 시 예약 데이터 기반으로 계산)
        return UserTier.AMATEUR;
    }
    
    /**
     * 주간 매치 현황 메시지를 생성합니다.
     */
    private String generateWeeklyMatchSummary() {
        return "[축구매치] 새로운 주가 시작되었습니다! 이번 주도 즐거운 축구 매치에 참여해보세요. ⚽ " +
               "빈 자리 알림을 받으시려면 앱에서 확인해주세요!";
    } 
    @Override
    public MatchNotificationSubscriptionResponseDto subscribe(MatchNotificationSubscriptionRequestDto requestDto, UserResponseDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Stadium stadium = null;
        if (requestDto.getStadiumId() != null) {
            stadium = stadiumRepository.findById(requestDto.getStadiumId())
                    .orElseThrow(() -> new CustomException(VendorErrorCode.VENDOR_NOT_FOUND));
        }

        // 이미 구독 중인지 확인
        if (stadium != null && subscriptionRepository.existsByUserAndStadiumAndIsActiveTrue(user, stadium)) {
            throw new RuntimeException("이미 해당 경기장에 대한 알림을 구독 중입니다.");
        }

        MatchNotificationSubscription subscription = MatchNotificationSubscription.builder()
                .user(user)
                .stadium(stadium)
                .preferredTier(requestDto.getPreferredTier())
                .phoneNumber(requestDto.getPhoneNumber())
                .isActive(true)
                .build();

        subscription = subscriptionRepository.save(subscription);
        log.info("매치 알림 구독 등록 완료: User {}, Stadium {}", user.getId(), 
                stadium != null ? stadium.getId() : "ALL");

        return MatchNotificationSubscriptionResponseDto.fromEntity(subscription);
    }

    @Override
    public void unsubscribe(Long subscriptionId, UserResponseDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        MatchNotificationSubscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("구독 정보를 찾을 수 없습니다."));

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 구독만 취소할 수 있습니다.");
        }

        subscription.deactivate();
        subscriptionRepository.save(subscription);
        log.info("매치 알림 구독 취소: User {}, Subscription {}", user.getId(), subscriptionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchNotificationSubscriptionResponseDto> getMySubscriptions(UserResponseDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return subscriptionRepository.findByUserAndIsActiveTrue(user).stream()
                .map(MatchNotificationSubscriptionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void notifyAvailableMatch(StadiumResponseDto stadiumDto, UserTier averageTier, int availableSpots) {
        Stadium stadium = stadiumRepository.findById(stadiumDto.getId())
                .orElseThrow(() -> new RuntimeException("경기장을 찾을 수 없습니다."));
        // 최근 30분 내에 알림을 받지 않은 구독자들에게만 전송 (스팸 방지)
        LocalDateTime notBefore = LocalDateTime.now().minusMinutes(30);
        
        List<MatchNotificationSubscription> eligibleSubscribers = subscriptionRepository
                .findEligibleSubscribers(stadium, averageTier, notBefore);

        if (eligibleSubscribers.isEmpty()) {
            log.info("알림 대상 구독자가 없습니다. Stadium: {}, Tier: {}", stadium.getName(), averageTier);
            return;
        }

        String message = String.format(
                "[축구매치] %s에서 %s 티어 매치에 %d자리가 생겼습니다! 지금 참여하세요. 🏈",
                stadium.getName(),
                getTierDisplayName(averageTier),
                availableSpots
        );

        // SMS 전송
        List<SmsRequestDto> smsRequests = eligibleSubscribers.stream()
                .map(sub -> new SmsRequestDto(sub.getPhoneNumber(), message))
                .collect(Collectors.toList());

        try {
            smsUtil.sendBulkAndLog(smsRequests);
            
            // 알림 전송 시간 업데이트
            eligibleSubscribers.forEach(MatchNotificationSubscription::updateLastNotified);
            subscriptionRepository.saveAll(eligibleSubscribers);
            
            log.info("매치 알림 전송 완료: {} 명에게 전송", eligibleSubscribers.size());
        } catch (Exception e) {
            log.error("매치 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToSubscribers(StadiumResponseDto stadiumDto, UserTier tier, String message) {
        Stadium stadium = stadiumRepository.findById(stadiumDto.getId())
                .orElseThrow(() -> new RuntimeException("경기장을 찾을 수 없습니다."));
        List<MatchNotificationSubscription> subscribers = subscriptionRepository
                .findActiveSubscribersByStadiumAndTier(stadium, tier);

        if (subscribers.isEmpty()) {
            return;
        }

        List<SmsRequestDto> smsRequests = subscribers.stream()
                .map(sub -> new SmsRequestDto(sub.getPhoneNumber(), message))
                .collect(Collectors.toList());

        try {
            smsUtil.sendBulkAndLog(smsRequests);
            log.info("매치 알림 전송 완료: {} 명에게 전송", subscribers.size());
        } catch (Exception e) {
            log.error("매치 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    private String getTierDisplayName(UserTier tier) {
        return switch (tier) {
            case ROOKIE -> "루키";
            case BEGINNER -> "초보";
            case AMATEUR -> "아마추어";
            case SEMI_PRO -> "세미프로";
            case PRO -> "프로";
        };
    }
}