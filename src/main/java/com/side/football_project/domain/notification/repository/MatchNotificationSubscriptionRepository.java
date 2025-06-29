package com.side.football_project.domain.notification.repository;

import com.side.football_project.domain.notification.entity.MatchNotificationSubscription;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.type.UserTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchNotificationSubscriptionRepository extends JpaRepository<MatchNotificationSubscription, Long> {

    /**
     * 특정 사용자의 활성 구독 조회
     */
    List<MatchNotificationSubscription> findByUserAndIsActiveTrue(User user);

    /**
     * 특정 경기장에 대한 활성 구독자 조회
     */
    @Query("SELECT mns FROM MatchNotificationSubscription mns " +
           "WHERE mns.isActive = true " +
           "AND (mns.stadium IS NULL OR mns.stadium = :stadium) " +
           "AND (mns.preferredTier IS NULL OR mns.preferredTier = :tier)")
    List<MatchNotificationSubscription> findActiveSubscribersByStadiumAndTier(
            @Param("stadium") Stadium stadium, 
            @Param("tier") UserTier tier);

    /**
     * 특정 사용자의 특정 경기장 구독 존재 여부 확인
     */
    boolean existsByUserAndStadiumAndIsActiveTrue(User user, Stadium stadium);

    /**
     * 최근 알림을 받지 않은 활성 구독자 조회 (스팸 방지용)
     */
    @Query("SELECT mns FROM MatchNotificationSubscription mns " +
           "WHERE mns.isActive = true " +
           "AND (mns.stadium IS NULL OR mns.stadium = :stadium) " +
           "AND (mns.preferredTier IS NULL OR mns.preferredTier = :tier) " +
           "AND (mns.lastNotifiedAt IS NULL OR mns.lastNotifiedAt <= :notBefore)")
    List<MatchNotificationSubscription> findEligibleSubscribers(
            @Param("stadium") Stadium stadium,
            @Param("tier") UserTier tier,
            @Param("notBefore") LocalDateTime notBefore);

    /**
     * 사용자의 특정 경기장 구독 조회
     */
    Optional<MatchNotificationSubscription> findByUserAndStadiumAndIsActiveTrue(User user, Stadium stadium);
    
    /**
     * 모든 활성 구독 조회 (주간 알림용)
     */
    List<MatchNotificationSubscription> findByIsActiveTrue();
}