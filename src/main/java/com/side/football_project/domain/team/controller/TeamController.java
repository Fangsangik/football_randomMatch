package com.side.football_project.domain.team.controller;

import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.service.TeamService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    /**
     * 팀 생성
     *
     * @param requestDto 팀 생성에 필요한 데이터
     * @return 생성된 팀 정보
     */
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(teamService.createTeam(requestDto, user));
    }

    /**
     * 팀 조회
     * @param teamId 팀 ID
     * @return 조회된 팀 정보
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> findTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.findTeam(teamId));
    }

    /**
     * 팀 수정
     * @param teamId 팀 ID
     * @param requestDto 팀 수정에 필요한 데이터
     * @return 팀 수정 완료 메시지
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<String> updateTeam(@PathVariable Long teamId,
                                             @RequestBody TeamRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.updateTeam(teamId, requestDto, user);
        return ResponseEntity.ok("팀 정보 수정이 완료되었습니다.");
    }

    @PatchMapping("/{teamId}/join")
    public ResponseEntity<String> joinTeam(@PathVariable Long teamId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.joinTeam(teamId, user);
        return ResponseEntity.ok("팀에 가입되었습니다.");
    }

    /**
     * 팀 삭제
     * @param teamId 팀 ID
     * @return 팀 삭제 완료 메시지
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok("팀이 삭제되었습니다.");
    }
}
