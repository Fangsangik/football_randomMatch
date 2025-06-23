package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.*;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.service.TeamService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import com.side.football_project.domain.reservation.entity.Reservation;
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
public class UserReservationServiceImpl implements UserReservationService {

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
                .reservationDate(java.time.LocalDate.now()) // 기본값
                .startTime(java.time.LocalTime.of(10, 0)) // 기본값
                .endTime(java.time.LocalTime.of(12, 0)) // 기본값
                .build();

        reservationRepository.save(reservation);
        stadium.increaseCurrentReservationCount();

        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public ReservationResponseDto findReservation(Long id, User user) {
        Reservation reservation = reservationRepository.findReservationByIdOrElseThrow(id);
        
        // 예약 소유자만 조회 가능하도록 권한 검증
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new CustomException(ReservationErrorCode.RESERVATION_ACCESS_DENIED);
        }
        
        return ReservationResponseDto.toEntity(reservation);
    }

    @Override
    public Page<GetMyReservationResponseDto> findMyReservations(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationRepository.findReservationsByUserId(user.getId(), pageable);
        return reservations.map(GetMyReservationResponseDto::toEntityList);
    }

    @Override
    public Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Reservation> reservation = reservationRepository.findReservationByStadium(id, pageable);
        return reservation.map(ReservationResponseDto::toEntity);
    }

    public Page<AvailableChekedReservationResponseDto> findAvailableReservations(Long stadiumId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationRepository.findAvailableReservationsByStadium(stadiumId, pageable);
        return reservations.map(AvailableChekedReservationResponseDto::toEntity);
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
                .reservationDate(java.time.LocalDate.now()) // 기본값
                .startTime(java.time.LocalTime.of(10, 0)) // 기본값
                .endTime(java.time.LocalTime.of(12, 0)) // 기본값
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

    @Override
    @Transactional
    public ReservationResponseDto createUserReservation(CreateReservationRequestDto requestDto, User user) {
        User findUser = userService.findUserById(user.getId());
        Stadium stadium = stadiumService.findByIdWithLock(requestDto.getStadiumId());

        // 시간 충돌 검사
        boolean hasConflict = reservationRepository.existsConflictingReservation(
            stadium, 
            requestDto.getReservationDate(), 
            requestDto.getStartTime(), 
            requestDto.getEndTime()
        );

        if (hasConflict) {
            throw new IllegalStateException("선택한 시간대에 이미 예약이 있습니다.");
        }

        // 예약 시간 유효성 검사
        if (!requestDto.getStartTime().isBefore(requestDto.getEndTime())) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 이후일 수 없습니다.");
        }

        // 과거 날짜 예약 방지
        if (requestDto.getReservationDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜로는 예약할 수 없습니다.");
        }

        // 경기장 시간당 요금 계산 (임시로 10000원/시간)
        long hours = java.time.Duration.between(requestDto.getStartTime(), requestDto.getEndTime()).toHours();
        int totalPrice = (int) (hours * 10000);

        Reservation reservation = Reservation.builder()
                .stadium(stadium)
                .user(findUser)
                .reservationDate(requestDto.getReservationDate())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .totalPrice(totalPrice)
                .memo(requestDto.getMemo())
                .build();

        reservationRepository.save(reservation);

        log.info("예약 생성 완료 - 사용자: {}, 경기장: {}, 날짜: {}, 시간: {}-{}", 
                findUser.getId(), stadium.getName(), 
                requestDto.getReservationDate(), requestDto.getStartTime(), requestDto.getEndTime());

        return ReservationResponseDto.toDto(reservation);
    }

    @Override
    @Transactional
    public void cancelUserReservation(Long reservationId, String reason, User user) {
        Reservation reservation = reservationRepository.findReservationByIdOrElseThrow(reservationId);

        // 예약 소유자 확인
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new CustomException(ReservationErrorCode.RESERVATION_ACCESS_DENIED);
        }

        // 예약 취소
        reservation.cancel(reason);
        reservationRepository.save(reservation);

        log.info("예약 취소 완료 - 사용자: {}, 예약ID: {}, 사유: {}", user.getId(), reservationId, reason);
    }
}
