package com.side.football_project.domain.user.dto;

import com.side.football_project.domain.user.type.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private final String email;

    private final String password;

    private final String confirmPassword;

    private final String name;

    private final String phoneNumber;

    private final int age;

    private final UserRole userRole;


    @Builder
    public UserRequestDto(String email, String password, String confirmPassword, String name, String phoneNumber, int age, UserRole role) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.userRole = role;
    }
}
