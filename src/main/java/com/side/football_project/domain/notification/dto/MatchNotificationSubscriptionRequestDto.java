package com.side.football_project.domain.notification.dto;

import com.side.football_project.domain.user.type.UserTier;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchNotificationSubscriptionRequestDto {

    private Long stadiumId; // null이면 모든 경기장
    private UserTier preferredTier; // null이면 모든 티어
    private String phoneNumber; // SMS 수신번호

    public MatchNotificationSubscriptionRequestDto(Long stadiumId, UserTier preferredTier, String phoneNumber) {
        this.stadiumId = stadiumId;
        this.preferredTier = preferredTier;
        this.phoneNumber = phoneNumber;
    }
}