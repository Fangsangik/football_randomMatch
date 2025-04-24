package com.side.football_project.domain.team.dto;

import com.side.football_project.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamResponseDto {
    private final Long id;

    private final String teamName;

    private final int headCount;


    private final LocalDateTime createdAt;

    @Builder
    public TeamResponseDto(Long id, String teamName, int headCount, LocalDateTime createdAt) {
        this.id = id;
        this.teamName = teamName;
        this.headCount = headCount;
        this.createdAt = createdAt;
    }

    public static TeamResponseDto toDto(Team team) {
        return new TeamResponseDto(
                team.getId(),
                team.getTeamName(),
                team.getHeadCount(),
                team.getCreatedAt()
        );
    }
}
