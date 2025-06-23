package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamMemberDetailDto;
import com.side.football_project.domain.team.dto.TeamMembershipStatusDto;
import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.dto.TeamsResponseDto;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.user.entity.User;

import java.util.List;

public interface TeamService {
    TeamResponseDto createTeam(TeamRequestDto requestDto, User user);
    void updateTeam(Long teamId, TeamRequestDto requestDto, User user);
    void deleteTeam(Long teamId, User user);
    TeamResponseDto findTeam(Long teamId);
    void joinTeam(Long teamId, User user);
    List<Team> findTeamEntitiesByIds(List<Long> teamIds);
    List<TeamResponseDto> getAllTeams();
    List<TeamMemberDetailDto> getTeamMembers(Long teamId);
    TeamMembershipStatusDto getMembershipStatus(Long teamId, User user);
    void leaveTeam(Long teamId, User user);
}
