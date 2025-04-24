package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private String name;
    private BigDecimal fee;
    private User user;
    private Stadium stadium;

    @Builder
    public ReservationResponseDto(Long id, String name, BigDecimal fee, User user, Stadium stadium) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.user = user;
        this.stadium = stadium;
    }

    public static ReservationResponseDto toEntity(Reservation reservation) {
        return ReservationResponseDto.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .fee(reservation.getFee())
                .user(reservation.getUser())
                .stadium(reservation.getStadium())
                .build();
    }

    public static Reservation toDto(ReservationResponseDto reservation) {
        return Reservation.builder()
                .name(reservation.getName())
                .fee(reservation.getFee())
                .user(reservation.getUser())
                .stadium(reservation.getStadium())
                .build();
    }
}
