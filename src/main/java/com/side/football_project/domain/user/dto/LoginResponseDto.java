package com.side.football_project.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String message;
    private String email;
    private String name;
    private String role;
    private String tier;
    
    public static LoginResponseDto of(String accessToken, Long expiresIn) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .message("로그인 성공")
                .build();
    }
}