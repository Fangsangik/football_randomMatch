package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.stadium.dto.StadiumRequestDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumUpdateDto;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;

    @PostMapping
    public ResponseEntity<StadiumResponseDto> createStadium(@RequestBody StadiumRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(stadiumService.createStadium(requestDto, user));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<StadiumResponseDto> findStadium(@PathVariable Long id) {
        return ResponseEntity.ok(stadiumService.findStadium(id));
    }

    @PatchMapping
    public ResponseEntity<StadiumUpdateDto> updateStadium(@RequestBody StadiumUpdateDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(stadiumService.updateStadium(requestDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStadium(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        stadiumService.deleteStadium(id, user);
        return ResponseEntity.noContent().build();
    }
}
