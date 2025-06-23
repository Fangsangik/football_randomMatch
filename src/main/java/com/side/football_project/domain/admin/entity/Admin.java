package com.side.football_project.domain.admin.entity;

import com.side.football_project.domain.user.type.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String adminKey;

    @Builder
    public Admin(String email, String password, String adminKey) {
        this.email = email;
        this.password = password;
        this.adminKey = adminKey;
    }

    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
