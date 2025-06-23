package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.dto.TeamMemberDetailDto;
import com.side.football_project.domain.team.dto.TeamMembershipStatusDto;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        // 리더 1명 반영
        team.updateCurrentHeadCount(1);
        team.takeAwayHeadCount();

        teamRepository.save(team);

        return TeamResponseDto.toDto(team);
    }


    @Override
    public TeamResponseDto findTeam(Long teamId) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        int currentHeadCount = (int) teamMemberRepository.countByTeam(team);
        return TeamResponseDto.toDto(team, currentHeadCount);
    }

    @Transactional
    @Override
    public void updateTeam(Long teamId, TeamRequestDto requestDto, User user) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        
        // 팀 리더 권한 확인
        TeamMember teamMember = teamMemberRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_MEMBER_NOT_FOUND));
        
        if (teamMember.getRole() != TeamRole.LEADER) {
            throw new CustomException(TeamErrorCode.TEAM_UPDATE_NOT_ALLOWED);
        }

        team.updateTeamInfo(requestDto.getTeamName(), requestDto.getHeadCount());
        teamRepository.save(team);
    }

    @Transactional
    @Override
    public void joinTeam(Long teamId, User user) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);

        // 이미 팀 멤버인지 확인
        if (teamMemberRepository.findByTeamAndUser(team, user).isPresent()) {
            throw new CustomException(TeamErrorCode.ALREADY_TEAM_MEMBER);
        }

        if (team.getHeadCount() <= 0) {
            throw new CustomException(TeamErrorCode.TEAM_FULL);
        }

        TeamMember newMember = TeamMember.builder()
                .team(team)
                .user(user)
                .role(TeamRole.MEMBER)
                .build();

        teamMemberRepository.save(newMember);

        // 정합성 유지: 실제 멤버 수 기반으로 업데이트
        int actualCount = (int) teamMemberRepository.countByTeam(team);
        team.updateCurrentHeadCount(actualCount);
        team.takeAwayHeadCount();

        teamRepository.save(team);
    }


    @Transactional
    @Override
    public void deleteTeam(Long teamId, User user) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        
        // 팀 리더 권한 확인
        TeamMember teamMember = teamMemberRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_MEMBER_NOT_FOUND));
        
        if (teamMember.getRole() != TeamRole.LEADER) {
            throw new CustomException(TeamErrorCode.TEAM_LEADER_AUTHORITY_REQUIRED);
        }
        
        // 팀 멤버들 먼저 삭제
        teamMemberRepository.deleteByTeam(team);
        // 팀 삭제
        teamRepository.delete(team);
    }

    @Override
    public List<Team> findTeamEntitiesByIds(List<Long> teamIds) {
        return teamRepository.findAllById(teamIds);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamResponseDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream()
                .map(TeamResponseDto::toDto)
                .toList();
    }

    @Override
    public List<TeamMemberDetailDto> getTeamMembers(Long teamId) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        List<TeamMember> teamMembers = teamMemberRepository.findByTeamOrderByCreatedAtDesc(team);
        
        return teamMembers.stream()
                .map(TeamMemberDetailDto::from)
                .toList();
    }

    @Override
    public TeamMembershipStatusDto getMembershipStatus(Long teamId, User user) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        Optional<TeamMember> teamMemberOpt = teamMemberRepository.findByTeamAndUser(team, user);
        
        if (teamMemberOpt.isPresent()) {
            TeamMember teamMember = teamMemberOpt.get();
            return TeamMembershipStatusDto.of(
                    true,
                    teamMember.getRole(),
                    teamMember.getCreatedAt()
            );
        } else {
            return TeamMembershipStatusDto.notMember();
        }
    }

    @Transactional
    @Override
    public void leaveTeam(Long teamId, User user) {
        Team team = teamRepository.findByTeamIdOrElseThrow(teamId);
        TeamMember teamMember = teamMemberRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_MEMBER_NOT_FOUND));

        if (teamMember.getRole() == TeamRole.LEADER) {
            throw new CustomException(TeamErrorCode.TEAM_LEADER_CANNOT_LEAVE);
        }

        teamMemberRepository.deleteByTeamAndUser(team, user);

        // 정합성 유지
        int actualCount = (int) teamMemberRepository.countByTeam(team);
        team.updateCurrentHeadCount(actualCount);
        team.addHeadCount();

        teamRepository.save(team);
    }


}
