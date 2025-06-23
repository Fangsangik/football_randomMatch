package com.side.football_project.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CancelReservationRequestDto {
    private String reason;
    
    public CancelReservationRequestDto(String reason) {
        this.reason = reason;
    }
}