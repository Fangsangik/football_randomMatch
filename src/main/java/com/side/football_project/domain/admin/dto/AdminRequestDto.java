package com.side.football_project.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRequestDto {
    private Long id;
    private String adminKey;
    private String email;
    private String password;

    public AdminRequestDto(Long id, String adminKey, String email, String password) {
        this.id = id;
        this.adminKey = adminKey;
        this.email = email;
        this.password = password;
    }
}
