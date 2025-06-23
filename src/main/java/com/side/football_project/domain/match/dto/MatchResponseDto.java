package com.side.football_project.domain.match.dto;


import com.side.football_project.domain.match.entity.Match;
import com.side.football_project.domain.match.entity.MatchUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MatchResponseDto {
    private Long id;
    private String matchName;
    private Long reservationId;
    private Long stadiumId;
    private LocalDateTime matchDate;
    private List<Long> matchUsers;

    @Builder
    public MatchResponseDto(Long id, String matchName, Long reservationId, Long stadiumId, LocalDateTime matchDate, List<Long> matchUsers) {
        this.id = id;
        this.matchName = matchName;
        this.reservationId = reservationId;
        this.stadiumId = stadiumId;
        this.matchDate = matchDate;
        this.matchUsers = matchUsers;
    }

    public static MatchResponseDto toDto(Match match) {
        return MatchResponseDto.builder()
                .id(match.getId())
                .matchName(match.getMatchName())
                .reservationId(match.getReservation().getId())
                .stadiumId(match.getStadium().getId())
                .matchDate(match.getMatchDate())
                .matchUsers(match.getMatchUsers()
                        .stream().map(MatchUser::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
