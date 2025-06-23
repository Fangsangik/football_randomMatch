package com.side.football_project.domain.match.repository;


import com.side.football_project.domain.match.entity.MatchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {

    List<MatchUser> findMatchById(Long matchId);

    boolean existsByUserIdAndMatchId(Long id, Long matchId);

    boolean existsByMatchId(Long matchId);
}
