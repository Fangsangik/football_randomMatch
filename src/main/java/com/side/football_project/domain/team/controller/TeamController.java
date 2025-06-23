package com.side.football_project.domain.team.controller;

import com.side.football_project.domain.team.dto.TeamMemberDetailDto;
import com.side.football_project.domain.team.dto.TeamMembershipStatusDto;
import com.side.football_project.domain.team.dto.TeamRequestDto;
import com.side.football_project.domain.team.dto.TeamResponseDto;
import com.side.football_project.domain.team.service.TeamService;
import com.side.football_project.domain.team.service.DataFixService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final DataFixService dataFixService;

    /**
     * 팀 목록 페이지 리다이렉트
     */
    @GetMapping("")
    public void teamListPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/teams/index.html");
    }

    /**
     * 팀 생성
     *
     * @param requestDto 팀 생성에 필요한 데이터
     * @return 생성된 팀 정보
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(teamService.createTeam(requestDto, user));
    }

    /**
     * 팀 조회
     * @param teamId 팀 ID
     * @return 조회된 팀 정보
     */
    @GetMapping("/{teamId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<TeamResponseDto> findTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.findTeam(teamId));
    }

    /**
     * 팀 수정
     * @param teamId 팀 ID
     * @param requestDto 팀 수정에 필요한 데이터
     * @return 팀 수정 완료 메시지
     */
    @PutMapping("/{teamId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<String> updateTeam(@PathVariable Long teamId,
                                             @RequestBody TeamRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.updateTeam(teamId, requestDto, user);
        return ResponseEntity.ok("팀 정보 수정이 완료되었습니다.");
    }

    /**
     * 팀 가입
     */
    @PatchMapping("/{teamId:[0-9]+}/join")
    @ResponseBody
    public ResponseEntity<String> joinTeam(@PathVariable Long teamId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.joinTeam(teamId, user);
        return ResponseEntity.ok("팀에 가입되었습니다.");
    }
    
    /**
     * 모든 팀 목록 조회 (API)
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<java.util.List<TeamResponseDto>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    /**
     * 팀 멤버 목록 조회
     * @param teamId 팀 ID
     * @return 팀 멤버 목록
     */
    @GetMapping("/{teamId:[0-9]+}/members")
    @ResponseBody
    public ResponseEntity<List<TeamMemberDetailDto>> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamMembers(teamId));
    }

    /**
     * 팀 멤버십 상태 조회
     * @param teamId 팀 ID
     * @param userDetails 현재 사용자 정보
     * @return 멤버십 상태
     */
    @GetMapping("/{teamId:[0-9]+}/membership-status")
    @ResponseBody
    public ResponseEntity<TeamMembershipStatusDto> getMembershipStatus(@PathVariable Long teamId,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(teamService.getMembershipStatus(teamId, user));
    }

    /**
     * 팀 탈퇴
     * @param teamId 팀 ID
     * @param userDetails 현재 사용자 정보
     * @return 탈퇴 완료 메시지
     */
    @DeleteMapping("/{teamId:[0-9]+}/leave")
    @ResponseBody
    public ResponseEntity<String> leaveTeam(@PathVariable Long teamId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.leaveTeam(teamId, user);
        return ResponseEntity.ok("팀에서 탈퇴되었습니다.");
    }

    /**
     * 데이터 수정 (임시)
     */
    @GetMapping("/fix-data")
    @ResponseBody
    public ResponseEntity<String> fixData() {
        dataFixService.fixTeamMemberCounts();
        return ResponseEntity.ok("팀 데이터가 수정되었습니다.");
    }

    /**
     * 팀 삭제
     * @param teamId 팀 ID
     * @return 팀 삭제 완료 메시지
     */
    @DeleteMapping("/{teamId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        teamService.deleteTeam(teamId, user);
        return ResponseEntity.ok("팀이 삭제되었습니다.");
    }
}