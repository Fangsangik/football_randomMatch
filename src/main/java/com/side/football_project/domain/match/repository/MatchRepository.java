package com.side.football_project.domain.match.repository;

import com.side.football_project.domain.match.domain.Match;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.MatchErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    default Match findMatchById(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(MatchErrorCode.MATCH_NOT_FOUND));
    }
}
