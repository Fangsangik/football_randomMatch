package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.*;
import com.side.football_project.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user);

    ReservationResponseDto findReservation(Long id, User user);

    Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size);

    ReservationResponseDto findReservationByStadium(Long id);

    Page<ReservationResponseDto> findReservationByUser(Long userId, int page, int size);

    TeamReservationResponseDto makeTeamReservation(TeamReservationRequestDto requestDto, User user);

    void deleteReservation(ReservationDeleteRequestDto requestDto, User user);

    Page<GetMyReservationResponseDto> findMyReservations(User user, int page, int size);

    // 새로운 사용자 예약 시스템용 메서드들
    ReservationResponseDto createUserReservation(CreateReservationRequestDto requestDto, User user);
    
    void cancelUserReservation(Long reservationId, String reason, User user);
    
    Page<AvailableChekedReservationResponseDto> findAvailableReservations(Long stadiumId, int page, int size);

}
