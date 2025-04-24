package com.side.football_project.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamRequestDto {
    private final String teamName;
    private final int headCount;
    private List<Long> userIds;


    @Builder
    public TeamRequestDto(String teamName, int headCount, List<Long> userIds) {
        this.teamName = teamName;
        this.headCount = headCount;
        this.userIds = userIds;
    }
}
