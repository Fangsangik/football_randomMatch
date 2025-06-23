package com.side.football_project.domain.team.service;

import com.side.football_project.domain.team.entity.Team;
import com.side.football_project.domain.team.repository.TeamMemberRepository;
import com.side.football_project.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataFixService {
    
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    
    @Transactional
    public void fixTeamMemberCounts() {
        List<Team> teams = teamRepository.findAll();
        
        for (Team team : teams) {
            // 실제 팀 멤버 수 계산
            long actualMemberCount = teamMemberRepository.countByTeam(team);
            
            // currentHeadCount 수정
            team.updateCurrentHeadCount((int) actualMemberCount);
            
            log.info("팀 [{}] 멤버 수 수정: {} -> {}", 
                     team.getTeamName(), team.getCurrentHeadCount(), actualMemberCount);
        }
        
        teamRepository.saveAll(teams);
        log.info("모든 팀의 멤버 수가 수정되었습니다.");
    }
}