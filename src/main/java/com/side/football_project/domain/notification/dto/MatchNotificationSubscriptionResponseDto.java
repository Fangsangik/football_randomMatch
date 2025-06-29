package com.side.football_project.domain.notification.dto;

import com.side.football_project.domain.notification.entity.MatchNotificationSubscription;
import com.side.football_project.domain.user.type.UserTier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MatchNotificationSubscriptionResponseDto {

    private Long id;
    private Long userId;
    private Long stadiumId;
    private String stadiumName;
    private UserTier preferredTier;
    private String phoneNumber;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastNotifiedAt;

    public static MatchNotificationSubscriptionResponseDto fromEntity(MatchNotificationSubscription subscription) {
        return MatchNotificationSubscriptionResponseDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .stadiumId(subscription.getStadium() != null ? subscription.getStadium().getId() : null)
                .stadiumName(subscription.getStadium() != null ? subscription.getStadium().getName() : "모든 경기장")
                .preferredTier(subscription.getPreferredTier())
                .phoneNumber(subscription.getPhoneNumber())
                .isActive(subscription.isActive())
                .createdAt(subscription.getCreatedAt())
                .lastNotifiedAt(subscription.getLastNotifiedAt())
                .build();
    }
}