package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.*;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.dto.TeamsResponseDto;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.service.TeamService;
import com.side.football_project.domain.team.service.TeamServiceImpl;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.domain.reservation.domain.Reservation;
import com.side.football_project.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final StadiumService stadiumService;
    private final TeamService teamService;

    @Transactional
    @Override
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        Stadium stadium = stadiumService.findByIdWithLock(requestDto.getStadiumId());

        if (stadium.isFullyBooked()) {
            throw new IllegalStateException("이미 예약이 완료된 경기장입니다.");
        }

        Reservation reservation = Reservation.builder()
                .name(requestDto.getName())
                .fee(requestDto.getFee())
                .user(findUser)
                .stadium(stadium)
                .build();

        reservationRepository.save(reservation);
        stadium.increaseCurrentReservationCount();

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public ReservationResponseDto findReservation(Long id) {
        Reservation reservation = reservationRepository.findReservationByIdOrElseThrow(id);
        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Reservation> reservation = reservationRepository.findReservationByStadium(id, pageable);
        return reservation.map(ReservationResponseDto::toEntity);
    }

    @Override
    public ReservationResponseDto findReservationByStadium(Long stadiumId) {
        Reservation reservation = reservationRepository.findReservationByStadiumId(stadiumId)
                .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public Page<ReservationResponseDto> findReservationByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservation = reservationRepository.findReservationByUser(userId, pageable);
        return reservation.map(ReservationResponseDto::toEntity);
    }

    @Transactional
    @Override
    public TeamReservationResponseDto makeTeamReservation(TeamReservationRequestDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        Stadium stadium = stadiumService.findByIdWithLock(requestDto.getStadiumId());

        if (stadium.isFullyBooked()) {
            throw new IllegalStateException("이미 예약이 완료된 경기장입니다.");
        }

        Reservation reservation = Reservation.builder()
                .stadium(stadium)
                .user(findUser)
                .name(requestDto.getTeamName())
                .fee(requestDto.getFee())
                .build();

        List<Team> teams = teamService.findTeamEntitiesByIds(requestDto.getTeamIds());

        teams.forEach(reservation::addTeam);

        long distinctCounts = teams.stream()
                .map(Team::getHeadCount)
                .distinct()
                .count();
        if (distinctCounts > 1) {
            throw new CustomException(ReservationErrorCode.RESERVATION_NOT_CREATED);
        }

        reservationRepository.save(reservation);
        stadium.increaseCurrentReservationCount();

        return TeamReservationResponseDto.toEntity(reservation);
    }

    @Transactional
    @Override
    public void deleteReservation(ReservationDeleteRequestDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        Reservation reservation = reservationRepository.findReservationByIdOrElseThrow(requestDto.getId());

        if (!reservation.getUser().getId().equals(findUser.getId())) {
            throw new CustomException(UserErrorCode.USER_NOT_MATCH);
        }

        Stadium stadium = reservation.getStadium();

        reservationRepository.delete(reservation);
        stadium.decreaseCurrentReservationCount();
    }
}
