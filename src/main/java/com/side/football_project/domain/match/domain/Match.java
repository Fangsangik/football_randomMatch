package com.side.football_project.domain.match.domain;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`match`")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matchName;
    private LocalDateTime matchDate;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchUser> matchUsers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    private boolean isCompleted;

    @Builder
    public Match(String matchName, LocalDateTime matchDate, List<MatchUser> matchUsers, Reservation reservation, Stadium stadium, boolean isCompleted) {
        this.matchName = matchName;
        this.matchDate = matchDate;
        this.matchUsers = matchUsers;
        this.reservation = reservation;
        this.stadium = stadium;
        this.isCompleted = isCompleted;
    }

    public void markedAsCompleted() {
        this.isCompleted = true;
    }
}
