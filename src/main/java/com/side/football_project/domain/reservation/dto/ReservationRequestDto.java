package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {

    private String name;
    private BigDecimal fee;
    private Long userId;
    private Long stadiumId;

    @Builder
    public ReservationRequestDto(String name, BigDecimal fee, Long userId, Long stadiumId) {
        this.name = name;
        this.fee = fee;
        this.userId = userId;
        this.stadiumId = stadiumId;
    }
}
