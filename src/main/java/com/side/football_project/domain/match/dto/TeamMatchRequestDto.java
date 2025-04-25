package com.side.football_project.domain.match.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamMatchRequestDto {

    private String MatchName;
    private Long reservationId;
    private Long stadiumId;
    private List<Long> teamIds;
    private List<String> teamName;
    private int headCount;

    public TeamMatchRequestDto(String matchName, Long reservationId, Long stadiumId, List<Long> teamIds, List<String> teamName, int headCount) {
        MatchName = matchName;
        this.reservationId = reservationId;
        this.stadiumId = stadiumId;
        this.teamIds = teamIds;
        this.teamName = teamName;
        this.headCount = headCount;
    }
}
