package com.side.football_project.domain.match.dto;

import com.side.football_project.domain.match.entity.Match;
import com.side.football_project.domain.team.dto.TeamsResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamMatchResponseDto {

    private Long id;
    private List<TeamsResponseDto> teams;
    private int headCount;

    @Builder
    public TeamMatchResponseDto(Long id, List<TeamsResponseDto> teams, int headCount) {
        this.id = id;
        this.teams = teams;
        this.headCount = headCount;
    }

    public static TeamMatchResponseDto fromEntity(Match match) {
        List<TeamsResponseDto> teams = match.getTeams().stream()
                .map(TeamsResponseDto::toDto)
                .toList();

        int headCount = match.getMatchUsers().size();


        return TeamMatchResponseDto.builder()
                .id(match.getId())
                .teams(teams)
                .headCount(headCount)
                .build();
    }
}
