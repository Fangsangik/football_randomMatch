package com.side.football_project.domain.team.dto;

import com.side.football_project.domain.team.entity.Team;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamsResponseDto {
    private List<Team> teams;

    public TeamsResponseDto(List<Team> teams) {
        this.teams = teams;
    }

    public static TeamsResponseDto toDto(Team team) {
        return new TeamsResponseDto(
                List.of(team)
        );
    }
}
