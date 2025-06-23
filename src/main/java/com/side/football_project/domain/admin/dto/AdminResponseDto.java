package com.side.football_project.domain.user.dto;

import com.side.football_project.domain.user.entity.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminResponseDto {

    private String email;

    @Builder
    public AdminResponseDto(String email) {
        this.email = email;
    }

    public static AdminResponseDto toEntity(Admin newAdmin) {
       AdminResponseDto admin = AdminResponseDto.builder()
                .email(newAdmin.getEmail())
                .build();
        return admin;
    }
}
