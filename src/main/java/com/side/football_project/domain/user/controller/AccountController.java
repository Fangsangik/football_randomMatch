package com.side.football_project.domain.user.controller;

import com.side.football_project.domain.user.dto.LoginRequestDto;
import com.side.football_project.domain.user.dto.LoginResponseDto;
import com.side.football_project.domain.user.dto.UserRequestDto;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.service.UserService;
import com.side.football_project.global.security.auth.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    /**
     * 회원 가입 페이지 리다이렉트
     */
    @GetMapping("/signup")
    public String signUpPage() {
        return "redirect:/auth/signup.html";
    }

    /**
     * 회원 가입
     * @param requestDto 회원 가입에 필요한 데이터 {@link UserRequestDto}
     * @return 가입된 회원 정보 {@link UserResponseDto}
     */
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.createUser(requestDto));
    }

    /**
     * 로그인 페이지 리다이렉트
     */
    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/auth/login.html";
    }

    /**
     * 로그인
     * @param requestDto 로그인에 필요한 데이터
     * @return JWT 토큰과 로그인 정보
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto loginResponse = userService.login(requestDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request){
        userService.logout(request);
        return ResponseEntity.ok(Map.of("message", "로그아웃이 완료되었습니다."));
    }

    /**
     * 토큰 갱신
     * @param request HTTP 요청 (Authorization 헤더에서 리프레시 토큰 추출)
     * @return 새로운 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request) {
        LoginResponseDto refreshResponse = userService.refreshToken(request);
        return ResponseEntity.ok(refreshResponse);
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/auth/logout.html";
    }
}
