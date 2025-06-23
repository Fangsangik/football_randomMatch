package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvailableChekedReservationResponseDto {

    private Long reservationId;
    private String stadiumName;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    @Builder
    public AvailableChekedReservationResponseDto(Long reservationId, String stadiumName, String date, String startTime, String endTime, String status) {
        this.reservationId = reservationId;
        this.stadiumName = stadiumName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static AvailableChekedReservationResponseDto toEntity(Reservation reservation) {
        return AvailableChekedReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .stadiumName(reservation.getStadium().getName())
                .startTime(reservation.getStartTime().toString())
                .endTime(reservation.getEndTime().toString())
                .status(reservation.getStatus().name())
                .build();
    }
}
