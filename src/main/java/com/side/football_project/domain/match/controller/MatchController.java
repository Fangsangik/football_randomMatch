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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchUserService matchUserService;
    private final RandomService randomService;

    @PostMapping
    public ResponseEntity<MatchResponseDto> create(@RequestBody MatchRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(matchUserService.createMatch(requestDto, user));
    }

    @PostMapping("/{matchId}/ratings")
    public ResponseEntity<String> rate(@PathVariable Long matchId,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody List<MatchRatingRequestDto> ratings) {
        User user = UserDetailsUtil.getUser(userDetails);
        matchUserService.submitMatchRatings(matchId, user, ratings);
        return ResponseEntity.ok("평가가 완료되었습니다.");
    }

    @PostMapping("/{matchId}/complete")
    public ResponseEntity<String> complete(@PathVariable Long matchId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        matchUserService.completeMatch(matchId, user);
        return ResponseEntity.ok("경기가 종료되었습니다.");
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinMatch(@RequestParam Long userId) {
        randomService.joinMatch(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/team")
    public ResponseEntity<TeamMatchResponseDto> teamMatch(@RequestBody TeamMatchRequestDto requestDto,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(matchUserService.teamMatch(requestDto, user));
    }

    @PostMapping("/fill/{stadiumId}")
    public ResponseEntity<Void> fillEmptySpot(@PathVariable Long stadiumId) {
        randomService.fillEmptySpot(stadiumId);
        return ResponseEntity.ok().build();
    }
}
