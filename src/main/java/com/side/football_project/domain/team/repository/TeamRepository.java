package com.side.football_project.domain.team.repository;

import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.TeamErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    default Team findByTeamIdOrElseThrow(Long teamId) {
        return findById(teamId).orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
    }
}
