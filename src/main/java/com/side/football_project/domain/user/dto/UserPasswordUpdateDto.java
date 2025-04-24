package com.side.football_project.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPasswordUpdateDto {
    private final String currentPassword;

    private final String newPassword;

    private final String newPasswordConfirm;

    @Builder
    public UserPasswordUpdateDto(String currentPassword, String newPassword, String newPasswordConfirm) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
