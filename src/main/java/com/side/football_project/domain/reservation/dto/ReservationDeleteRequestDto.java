package com.side.football_project.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationDeleteRequestDto {
    private Long id;

    public ReservationDeleteRequestDto(Long id) {
        this.id = id;
    }
}
