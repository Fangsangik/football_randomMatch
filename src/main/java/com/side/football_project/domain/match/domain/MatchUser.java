package com.side.football_project.domain.match.domain;

import com.side.football_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Entity
@NoArgsConstructor
public class MatchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

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
