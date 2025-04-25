package com.side.football_project.domain.team.entity;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private TeamRole role;

    @Builder
    public TeamMember(Team team, User user, TeamRole role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }

    public void addTeam(Team team) {
        this.team = team;
        team.getTeamMembers().add(this);
    }
}

