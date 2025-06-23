package com.side.football_project.domain.team.dto;

import com.side.football_project.domain.team.entity.TeamRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamMembershipStatusDto {
    private final boolean isMember;
    private final TeamRole role;
    private final LocalDateTime joinedDate;

    @Builder
    public TeamMembershipStatusDto(boolean isMember, TeamRole role, LocalDateTime joinedDate) {
        this.isMember = isMember;
        this.role = role;
        this.joinedDate = joinedDate;
    }

    public static TeamMembershipStatusDto of(boolean isMember, TeamRole role, LocalDateTime joinedDate) {
        return TeamMembershipStatusDto.builder()
                .isMember(isMember)
                .role(role)
                .joinedDate(joinedDate)
                .build();
    }

    public static TeamMembershipStatusDto notMember() {
        return TeamMembershipStatusDto.builder()
                .isMember(false)
                .role(null)
                .joinedDate(null)
                .build();
    }
}