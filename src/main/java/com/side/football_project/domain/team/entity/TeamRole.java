package com.side.football_project.domain.team.entity;

import lombok.Getter;

@Getter
public enum TeamRole {
    MEMBER("ROLE_MEMBER"),
    LEADER("ROLE_LEADER");

    private String roleName;

    TeamRole(String roleName) {
        this.roleName = roleName;
    }
}
