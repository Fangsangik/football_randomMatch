package com.side.football_project.domain.user.dto;

import com.side.football_project.domain.user.type.UserTier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindUserWithRadiusDto {
    private Double centerLatitude;
    private Double centerLongitude;
    private int radiusKm;
    private UserTier userTier;
    private String stadiumName;
    private int availableSpots;

    @Builder
    public FindUserWithRadiusDto(Double centerLatitude, Double centerLongitude, int radiusKm, UserTier userTier) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radiusKm = radiusKm;
        this.userTier = userTier;
    }
}
