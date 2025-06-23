package com.side.football_project.domain.team.repository;

import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.entity.TeamMember;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    default TeamMember findByTeamMemberIdOrElseThrow(Long teamMemberId) {
        return findById(teamMemberId).orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
    }
    
    Optional<TeamMember> findByTeamAndUser(Team team, User user);
    
    List<TeamMember> findByTeamOrderByCreatedAtDesc(Team team);
    
    void deleteByTeam(Team team);
    
    void deleteByTeamAndUser(Team team, User user);
    
    long countByTeam(Team team);
}
