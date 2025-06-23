package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.*;
import com.side.football_project.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user);

    ReservationResponseDto findReservation(Long id, User user);

    Page<ReservationResponseDto> findAllReservationByStadium(Long id, int page, int size);

    ReservationResponseDto findReservationByStadium(Long id);

    Page<ReservationResponseDto> findReservationByUser(Long userId, int page, int size);


    TeamReservationResponseDto makeTeamReservation(TeamReservationRequestDto requestDto, User user);

    void deleteReservation(ReservationDeleteRequestDto requestDto, User user);

}
