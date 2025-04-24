package com.side.football_project.domain.match.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MatchRequestDto {
    private String matchName;
    private Long reservationId;
    private Long stadiumId;
    private LocalDateTime matchDate;
    private List<Long> matchUsers;

    public MatchRequestDto(String matchName, Long reservationId, Long stadiumId, LocalDateTime matchDate, List<Long> matchUsers) {
        this.matchName = matchName;
        this.reservationId = reservationId;
        this.stadiumId = stadiumId;
        this.matchDate = matchDate;
        this.matchUsers = matchUsers;
    }
}
