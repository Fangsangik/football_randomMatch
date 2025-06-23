package com.side.football_project.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRequestDto {
    private String adminKey;
    private String email;
    private String password;

    public AdminRequestDto(String adminKey, String email, String password) {
        this.adminKey = adminKey;
        this.email = email;
        this.password = password;
    }
}
