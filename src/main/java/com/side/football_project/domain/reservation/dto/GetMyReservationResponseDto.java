package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetMyReservationResponseDto {
    private Long reservationId;
    private String stadiumName;
    private String reservationDate;
    private String startTime;
    private String endTime;
    private String status;

    @Builder
    public GetMyReservationResponseDto(Long reservationId, String stadiumName, String reservationDate, String startTime, String endTime, String status) {
        this.reservationId = reservationId;
        this.stadiumName = stadiumName;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static GetMyReservationResponseDto toEntityList(Reservation reservation) {
        return GetMyReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .stadiumName(reservation.getStadium().getName())
                .reservationDate(reservation.getReservationDate().toString())
                .startTime(reservation.getStartTime().toString())
                .endTime(reservation.getEndTime().toString())
                .status(reservation.getStatus().name())
                .build();
    }
}
