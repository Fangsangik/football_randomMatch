package com.side.football_project.domain.match.service;

import com.side.football_project.domain.match.domain.Match;
import com.side.football_project.domain.match.domain.MatchUser;
import com.side.football_project.domain.match.dto.*;
import com.side.football_project.domain.match.repository.MatchRepository;
import com.side.football_project.domain.match.repository.MatchUserRepository;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.service.TeamService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.MatchErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;

import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchUserServiceImpl implements MatchUserService {
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final UserService userService;
    private final ReservationService reservationService;
    private final TeamService teamService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public MatchResponseDto createMatch (MatchRequestDto requestDto, User user) {

        validateUserRole(user);

        ReservationResponseDto reservation = reservationService.findReservation(requestDto.getReservationId());

        List<User> users = userService.getAllUser();

        Match match = Match.builder()
                .matchName(requestDto.getMatchName())
                .reservation(ReservationResponseDto.toDto(reservation))
                .matchDate(requestDto.getMatchDate())
                .stadium(reservation.getStadium())
                .isCompleted(false)
                .build();

        matchRepository.save(match);

        for (User matchUser : users) {
            MatchUser matchUserEntity = MatchUser.builder()
                    .match(match)
                    .user(matchUser)
                    .build();
            matchUserRepository.save(matchUserEntity);
        }

        return MatchResponseDto.toDto(match);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public TeamMatchResponseDto teamMatch (TeamMatchRequestDto requestDto, User user) {

        validateUserRole(user);

        ReservationResponseDto reservation = reservationService.findReservation(requestDto.getReservationId());

        Match match = Match.builder()
                .matchName(requestDto.getMatchName())
                .reservation(ReservationResponseDto.toDto(reservation))
                .stadium(reservation.getStadium())
                .isCompleted(false)
                .build();

        List<Long> matchTeamIds = requestDto.getTeamIds();
        List<Team> matchTeams = teamService.findTeamEntitiesByIds(matchTeamIds);

        matchTeams.forEach(match::addMatchTeam);

        matchRepository.save(match);

        return TeamMatchResponseDto.fromEntity(match);

    }

    /**
     * 경기 종료 후 평점 등록
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void submitMatchRatings(Long matchId, User user, List<MatchRatingRequestDto> ratings) {

        validateUserRole(user);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));

        boolean isMatchCompleted = matchUserRepository.existsByMatchId(matchId);
        if (!isMatchCompleted) {
            throw new CustomException(MatchErrorCode.MATCH_NOT_FINISHED);
        }

        for (MatchRatingRequestDto ratingRequest : ratings) {
            User findUser = userService.findUserById(user.getId());

            // 중복 평점 방지
            boolean exists = matchUserRepository.existsByUserIdAndMatchId(findUser.getId(), matchId);
            if (exists) {
                throw new IllegalStateException("이미 해당 유저의 평점이 등록되었습니다.");
            }

            MatchUser matchParticipant = MatchUser.builder()
                    .match(match)
                    .rate(ratingRequest.getRating())
                    .user(findUser)
                    .build();
            matchUserRepository.save(matchParticipant);
        }
    }

    /**
     * 경기 종료 후 모든 참가자의 UserTier 업데이트
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void completeMatch(Long matchId, User user) {

        validateUserRole(user);

        boolean isMatchCompleted = matchUserRepository.existsByMatchId(matchId);

        if (!isMatchCompleted) {
            throw new CustomException(MatchErrorCode.MATCH_NOT_FINISHED);
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));

        match.markedAsCompleted();

        List<MatchUser> matchById = matchUserRepository.findMatchById(match.getId());
        for (MatchUser matchUser : matchById) {
            userService.updateUserTier(matchUser.getUser().getId(), user);
        }
    }

    private static void validateUserRole(User user) {
        if (!UserRole.ADMIN.equals(user.getRole())) {
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }
    }
}
