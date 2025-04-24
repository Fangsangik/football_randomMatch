package com.side.football_project.domain.user.dto;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long id;

    private final String email;

    private final String name;

    private final String phoneNumber;

    private final int age;

    private final UserTier userTier;

    private final UserRole userRole;

    private final LocalDateTime createdAt;


    @Builder
    public UserResponseDto(Long id, String email, String name, String phoneNumber, int age, UserTier userTier, UserRole userRole, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.userTier = userTier;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAge(),
                user.getTier(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public static User toEntity(UserResponseDto userResponseDto) {
        return User.builder()
                .email(userResponseDto.getEmail())
                .name(userResponseDto.getName())
                .phoneNumber(userResponseDto.getPhoneNumber())
                .age(userResponseDto.getAge())
                .role(userResponseDto.getUserRole())
                .build();
    }
}
