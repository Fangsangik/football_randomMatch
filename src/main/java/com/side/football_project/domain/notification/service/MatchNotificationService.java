package com.side.football_project.domain.notification.service;

import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionRequestDto;
import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionResponseDto;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.type.UserTier;

import java.util.List;

public interface MatchNotificationService {

    /**
     * 매치 알림 구독 등록
     */
    MatchNotificationSubscriptionResponseDto subscribe(MatchNotificationSubscriptionRequestDto requestDto, UserResponseDto userDto);

    /**
     * 매치 알림 구독 취소
     */
    void unsubscribe(Long subscriptionId, UserResponseDto userDto);

    /**
     * 사용자의 구독 목록 조회
     */
    List<MatchNotificationSubscriptionResponseDto> getMySubscriptions(UserResponseDto userDto);

    /**
     * 매치 빈 자리 알림 전송 (시스템 내부 호출용)
     */
    void notifyAvailableMatch(StadiumResponseDto stadiumDto, UserTier averageTier, int availableSpots);

    /**
     * 특정 경기장의 구독자들에게 알림 전송
     */
    void sendNotificationToSubscribers(StadiumResponseDto stadiumDto, UserTier tier, String message);
}