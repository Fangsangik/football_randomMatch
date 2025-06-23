package com.side.football_project.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.football_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MatchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Double rate;

    @Builder
    public MatchUser(Match match, User user, Double rate) {
        this.match = match;
        this.user = user;
        this.rate = rate;
    }
}
