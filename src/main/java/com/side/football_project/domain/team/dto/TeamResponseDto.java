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

    private final int currentHeadCount;

    private final LocalDateTime createdAt;

    @Builder
    public TeamResponseDto(Long id, String teamName, int headCount, int currentHeadCount, LocalDateTime createdAt) {
        this.id = id;
        this.teamName = teamName;
        this.headCount = headCount;
        this.currentHeadCount = currentHeadCount;
        this.createdAt = createdAt;
    }

    public static TeamResponseDto toDto(Team team) {
        return new TeamResponseDto(
                team.getId(),
                team.getTeamName(),
                team.getHeadCount(),
                team.getCurrentHeadCount(),
                team.getCreatedAt()
        );
    }

    public static TeamResponseDto toDto(Team team, int currentHeadCount) {
        return new TeamResponseDto(
                team.getId(),
                team.getTeamName(),
                team.getHeadCount(),
                currentHeadCount,
                team.getCreatedAt()
        );
    }
}


