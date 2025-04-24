package com.side.football_project.domain.team.repository;

import com.side.football_project.domain.team.entity.TeamMember;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    default TeamMember findByTeamMemberIdOrElseThrow(Long teamMemberId) {
        return findById(teamMemberId).orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
    }
}
