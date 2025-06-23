package com.side.football_project.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class CreateReservationRequestDto {
    private Long stadiumId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String memo;
    
    public CreateReservationRequestDto(Long stadiumId, LocalDate reservationDate, 
                                     LocalTime startTime, LocalTime endTime, String memo) {
        this.stadiumId = stadiumId;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
    }
}