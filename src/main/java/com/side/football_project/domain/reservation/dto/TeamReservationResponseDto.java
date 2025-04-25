package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.reservation.domain.Reservation;
import lombok.Getter;

@Getter
public class TeamReservationResponseDto {
    private Long id;

    public TeamReservationResponseDto(Long id) {
        this.id = id;
    }

    public static TeamReservationResponseDto toEntity(Reservation reservation) {
        return new TeamReservationResponseDto(
                reservation.getId()
        );
    }
}
