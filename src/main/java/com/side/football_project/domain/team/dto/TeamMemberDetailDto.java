package com.side.football_project.domain.team.dto;

import com.side.football_project.domain.team.entity.TeamMember;
import com.side.football_project.domain.team.entity.TeamRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamMemberDetailDto {
    private final Long memberId;
    private final String userName;
    private final String email;
    private final TeamRole role;
    private final LocalDateTime joinedDate;

    @Builder
    public TeamMemberDetailDto(Long memberId, String userName, String email, TeamRole role, LocalDateTime joinedDate) {
        this.memberId = memberId;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.joinedDate = joinedDate;
    }

    public static TeamMemberDetailDto from(TeamMember teamMember) {
        return TeamMemberDetailDto.builder()
                .memberId(teamMember.getId())
                .userName(teamMember.getUser().getName())
                .email(teamMember.getUser().getEmail())
                .role(teamMember.getRole())
                .joinedDate(teamMember.getCreatedAt())
                .build();
    }
}