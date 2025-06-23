package com.side.football_project.domain.match.controller;


import com.side.football_project.domain.match.dto.*;
import com.side.football_project.domain.match.service.MatchUserService;
import com.side.football_project.domain.match.service.RandomService;
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
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    /**
     * 매치 목록 페이지 리다이렉트
     */
    @GetMapping("")
    public void matchListPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/matches/index.html");
    }
    
    /**
     * 모든 매치 목록 조회 (API)
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<java.util.List<MatchResponseDto>> getAllMatches() {
        return ResponseEntity.ok(matchUserService.getAllMatches());
    }

    private final MatchUserService matchUserService;
    private final RandomService randomService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<MatchResponseDto> create(@RequestBody MatchRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(matchUserService.createMatch(requestDto, user));
    }

    @PostMapping("/{matchId:[0-9]+}/ratings")
    @ResponseBody
    public ResponseEntity<String> rate(@PathVariable Long matchId,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody List<MatchRatingRequestDto> ratings) {
        User user = UserDetailsUtil.getUser(userDetails);
        matchUserService.submitMatchRatings(matchId, user, ratings);
        return ResponseEntity.ok("평가가 완료되었습니다.");
    }

    @PostMapping("/{matchId:[0-9]+}/complete")
    @ResponseBody
    public ResponseEntity<String> complete(@PathVariable Long matchId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        matchUserService.completeMatch(matchId, user);
        return ResponseEntity.ok("경기가 종료되었습니다.");
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<Void> joinMatch(@AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        randomService.joinMatch(user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/team")
    @ResponseBody
    public ResponseEntity<TeamMatchResponseDto> teamMatch(@RequestBody TeamMatchRequestDto requestDto,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(matchUserService.teamMatch(requestDto, user));
    }

    @PostMapping("/fill/{stadiumId:[0-9]+}")
    @ResponseBody
    public ResponseEntity<Void> fillEmptySpot(@PathVariable Long stadiumId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        randomService.fillEmptySpot(stadiumId);
        return ResponseEntity.ok().build();
    }
}
