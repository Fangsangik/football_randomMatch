package com.side.football_project.domain.team.dto;

import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamMemberResponseDto {
    private final List<Team> teams;

    @Builder
    public TeamMemberResponseDto(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new CustomException(TeamErrorCode.TEAM_NOT_FOUND);
        }
        this.teams = teams;
    }

    public static TeamMemberResponseDto toDto(List<Team> teams) {
        return new TeamMemberResponseDto(teams);
    }
}
