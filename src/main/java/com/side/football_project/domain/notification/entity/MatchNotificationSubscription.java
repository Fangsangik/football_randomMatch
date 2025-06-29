package com.side.football_project.domain.notification.entity;

import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.type.UserTier;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_notification_subscription")
@Getter
@NoArgsConstructor
public class MatchNotificationSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium; // null이면 모든 경기장

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_tier")
    private UserTier preferredTier; // null이면 모든 티어

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_notified_at")
    private LocalDateTime lastNotifiedAt;

    @Builder
    public MatchNotificationSubscription(User user, Stadium stadium, UserTier preferredTier, 
                                       String phoneNumber, boolean isActive) {
        this.user = user;
        this.stadium = stadium;
        this.preferredTier = preferredTier;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    public void updateLastNotified() {
        this.lastNotifiedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}