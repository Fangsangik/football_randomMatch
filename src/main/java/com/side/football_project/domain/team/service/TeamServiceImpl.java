package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamMemberResponseDto;
import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.entity.TeamMember;
import com.side.football_project.domain.team.entity.TeamRole;
import com.side.football_project.domain.team.repository.TeamMemberRepository;
import com.side.football_project.domain.team.repository.TeamRepository;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import com.side.football_project.global.common.exception.type.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    @Override
    public TeamResponseDto createTeam(TeamRequestDto requestDto, User user) {
        Team team = Team.builder()
                .teamName(requestDto.getTeamName())
                .headCount(requestDto.getHeadCount())
                .build();

        TeamMember leader = TeamMember.builder()
                .team(team)
                .user(user)
                .role(TeamRole.LEADER)
                .build();

        team.addTeamMember(leader);
        teamRepository.save(team);

        return TeamResponseDto.toDto(team);
    }

    @Override
    public TeamResponseDto findTeam(Long teamId) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        return TeamResponseDto.toDto(team);
    }

    @Transactional
    @Override
    public void updateTeam(Long teamId, TeamRequestDto requestDto, User user) {

        if (!user.getId().equals(teamId)){
            throw new CustomException(UserErrorCode.NOT_ALLOWED);
        }

        TeamMember team = teamMemberRepository.findByTeamMemberIdOrElseThrow(teamId);
        if (TeamRole.LEADER.equals(team.getRole()))
        team.getTeam().updateTeamInfo(requestDto.getTeamName(), requestDto.getHeadCount());

        teamMemberRepository.save(team);
    }

    @Transactional
    @Override
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        teamRepository.delete(team);
    }

    @Transactional
    @Override
    public void joinTeam(Long teamId, User user) {
        TeamMember team = teamMemberRepository.findByTeamMemberIdOrElseThrow(teamId);

        if (team.getUser().getId().equals(user.getId())) {
            throw new CustomException(TeamErrorCode.ALREADY_TEAM_MEMBER);
        }

        if (team.getTeam().getHeadCount() <= 0) {
            throw new CustomException(TeamErrorCode.TEAM_FULL);
        }

        team.getTeam().takeAwayHeadCount();

        teamMemberRepository.save(team);
    }

    public TeamMemberResponseDto getTeamMembers(Long teamId) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        return TeamMemberResponseDto.toDto(List.of(team));
    }
}
