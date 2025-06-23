package com.side.football_project.domain.team.controller;

import com.side.football_project.domain.team.service.DataFixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DataFixController {
    
    private final DataFixService dataFixService;
    
    @GetMapping("/fix-team-counts")
    public ResponseEntity<String> fixTeamMemberCounts() {
        dataFixService.fixTeamMemberCounts();
        return ResponseEntity.ok("팀 멤버 수가 수정되었습니다.");
    }
}