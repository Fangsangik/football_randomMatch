package com.side.football_project.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.football_project.domain.match.entity.MatchUser;
import com.side.football_project.domain.shorts.entity.Shorts;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.user.type.UserTier;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25)
    private String name;

    @Column(length = 50)
    private String phoneNumber;

    private int age;

    @Enumerated(EnumType.STRING)
    private UserTier tier;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 100)
    private String password;

    @Column(length = 100)
    private String email;

    private double latitude;
    private double longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchUser> matchUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shorts> shortsList = new ArrayList<>();

    /**
     * 생성자
     */
    protected User() {}

    @Builder
    public User(String name, String phoneNumber, int age, UserTier tier, UserRole role, String password, String email, double latitude, double longitude, List<MatchUser> matchUsers, List<Shorts> shortsList) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.tier = tier;
        this.role = role;
        this.password = password;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.matchUsers = matchUsers;
        this.shortsList = shortsList;
    }

    /**
     * 서비스 편의 메서드
     */
    public void updateTier(UserTier tier) {
        this.tier = tier;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }
}
