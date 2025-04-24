package com.side.football_project.domain.team.entity;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25)
    private String teamName;

    private int headCount;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder
    public Team(String teamName, int headCount, List<TeamMember> teamMembers) {
        this.teamName = teamName;
        this.headCount = headCount;
        this.teamMembers = teamMembers;
    }

    /**
     * 서비스 편의 메서드
     */
    public void updateTeamInfo(String teamName, int headCount) {
        this.teamName = teamName;
        this.headCount = headCount;
    }

    public void takeAwayHeadCount() {
        if (this.headCount > 0) {
            this.headCount--;
        }
    }

    public void addTeamMember(TeamMember member) {
        this.teamMembers.add(member);
        member.addTeam(this);
    }
}
