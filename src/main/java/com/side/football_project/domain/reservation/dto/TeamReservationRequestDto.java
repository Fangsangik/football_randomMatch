package com.side.football_project.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamReservationRequestDto {

    List<Long> teamIds;
    private String teamName;
    private BigDecimal fee;
    private Long stadiumId;
    private Long teamId;

    public TeamReservationRequestDto(List<Long> teamIds, String teamName, BigDecimal fee, Long stadiumId, Long teamId) {
        this.teamIds = teamIds;
        this.teamName = teamName;
        this.fee = fee;
        this.stadiumId = stadiumId;
        this.teamId = teamId;
    }
}
