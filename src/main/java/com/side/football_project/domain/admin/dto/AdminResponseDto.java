package com.side.football_project.domain.admin.dto;

import com.side.football_project.domain.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminResponseDto {

    private Long id;
    private String email;
    private String accessToken;

    @Builder
    public AdminResponseDto(Long id, String email, String accessToken) {
        this.id = id;
        this.email = email;
        this.accessToken = accessToken;
    }

    public static AdminResponseDto toEntity(Admin newAdmin) {
       AdminResponseDto admin = AdminResponseDto.builder()
                .id(newAdmin.getId())
                .email(newAdmin.getEmail())
                .build();
        return admin;
    }
}
