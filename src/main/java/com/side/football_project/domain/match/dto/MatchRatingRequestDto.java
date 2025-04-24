package com.side.football_project.domain.match.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchRatingRequestDto {
    private Long userId;
    private double rating;

    public MatchRatingRequestDto(Long userId, double rating) {
        this.userId = userId;
        this.rating = rating;
    }
}
