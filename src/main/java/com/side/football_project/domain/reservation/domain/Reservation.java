package com.side.football_project.domain.reservation.domain;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal fee;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stadium stadium;

    @Builder
    public Reservation(String name, BigDecimal fee, User user, Stadium stadium) {
        this.name = name;
        this.fee = fee;
        this.user = user;
        this.stadium = stadium;
    }
}
