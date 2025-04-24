package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.user.entity.User;

public interface TeamService {
    TeamResponseDto createTeam(TeamRequestDto requestDto, User user);
    void updateTeam(Long teamId, TeamRequestDto requestDto, User user);
    void deleteTeam(Long teamId);
    TeamResponseDto findTeam(Long teamId);
    void joinTeam(Long teamId, User user);
}
