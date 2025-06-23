package com.side.football_project.domain.team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.football_project.domain.match.entity.Match;
import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25)
    private String teamName;

    private int headCount;
    private int currentHeadCount;

    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Team(String teamName, int headCount, int currentHeadCount, List<TeamMember> teamMembers, Match match, Reservation reservation) {
        this.teamName = teamName;
        this.headCount = headCount;
        this.currentHeadCount = currentHeadCount;
        this.teamMembers = teamMembers;
        this.match = match;
        this.reservation = reservation;
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

    public void addCurrentHeadCount() {
        this.currentHeadCount++;
    }

    public void addHeadCount() {
        this.headCount++;
    }

    public void takeAwayCurrentHeadCount() {
        if (this.currentHeadCount > 0) {
            this.currentHeadCount--;
        }
    }

    public void updateCurrentHeadCount(int count) {
        this.currentHeadCount = count;
    }

    public void addTeamMember(TeamMember member) {
        if (this.teamMembers == null) {
            this.teamMembers = new ArrayList<>();
        }
        this.teamMembers.add(member);
        member.addTeam(this);
    }

    public void addReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void applyMatch (Match match) {
        this.match = match;
        match.addMatchTeam(this);
    }
}
