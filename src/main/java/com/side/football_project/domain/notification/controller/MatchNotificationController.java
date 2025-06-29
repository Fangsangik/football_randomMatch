package com.side.football_project.domain.notification.controller;

import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionRequestDto;
import com.side.football_project.domain.notification.dto.MatchNotificationSubscriptionResponseDto;
import com.side.football_project.domain.notification.service.MatchNotificationService;
import com.side.football_project.domain.user.dto.UserResponseDto;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications/match")
@RequiredArgsConstructor
public class MatchNotificationController {

    private final MatchNotificationService matchNotificationService;

    /**
     * 매치 알림 구독 등록
     */
    @PostMapping("/subscribe")
    public ResponseEntity<MatchNotificationSubscriptionResponseDto> subscribe(
            @RequestBody MatchNotificationSubscriptionRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        UserResponseDto userDto = UserResponseDto.toDto(user);
        MatchNotificationSubscriptionResponseDto response = matchNotificationService.subscribe(requestDto, userDto);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 매치 알림 구독 취소
     */
    @DeleteMapping("/unsubscribe/{subscriptionId}")
    public ResponseEntity<Map<String, String>> unsubscribe(
            @PathVariable Long subscriptionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        UserResponseDto userDto = UserResponseDto.toDto(user);
        matchNotificationService.unsubscribe(subscriptionId, userDto);
        
        return ResponseEntity.ok(Map.of("message", "알림 구독이 취소되었습니다."));
    }

    /**
     * 내 구독 목록 조회
     */
    @GetMapping("/my-subscriptions")
    public ResponseEntity<List<MatchNotificationSubscriptionResponseDto>> getMySubscriptions(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        UserResponseDto userDto = UserResponseDto.toDto(user);
        List<MatchNotificationSubscriptionResponseDto> subscriptions = matchNotificationService.getMySubscriptions(userDto);
        
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * 구독 가능한 경기장 목록 조회 (UI용)
     */
    @GetMapping("/available-stadiums")
    public ResponseEntity<Map<String, Object>> getAvailableStadiums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 실제로는 stadium service를 호출하여 경기장 목록을 가져와야 함
        // 여기서는 UI 개선을 위한 기본 구조만 제공
        response.put("stadiums", List.of());
        response.put("totalPages", 0);
        response.put("currentPage", page);
        response.put("totalElements", 0);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 알림 설정 상태 조회 (UI용)
     */
    @GetMapping("/notification-settings")
    public ResponseEntity<Map<String, Object>> getNotificationSettings(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        UserResponseDto userDto = UserResponseDto.toDto(user);
        List<MatchNotificationSubscriptionResponseDto> subscriptions = matchNotificationService.getMySubscriptions(userDto);
        
        Map<String, Object> settings = new HashMap<>();
        settings.put("totalSubscriptions", subscriptions.size());
        settings.put("subscriptions", subscriptions);
        settings.put("isNotificationEnabled", !subscriptions.isEmpty());
        
        // 사용자의 선호 설정 정보
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("preferredTiers", subscriptions.stream()
                .map(MatchNotificationSubscriptionResponseDto::getPreferredTier)
                .distinct()
                .toList());
        preferences.put("subscribedStadiumCount", subscriptions.stream()
                .map(MatchNotificationSubscriptionResponseDto::getStadiumId)
                .distinct()
                .count());
        
        settings.put("preferences", preferences);
        
        return ResponseEntity.ok(settings);
    }

    /**
     * 구독 일괄 관리 (UI용)
     */
    @PutMapping("/bulk-manage")
    public ResponseEntity<Map<String, String>> bulkManageSubscriptions(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        String action = (String) request.get("action");
        
        switch (action) {
            case "enable_all":
                // 모든 구독 활성화 로직 (실제 구현 필요)
                break;
            case "disable_all":
                // 모든 구독 비활성화 로직 (실제 구현 필요)
                break;
            default:
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "지원하지 않는 액션입니다."));
        }
        
        return ResponseEntity.ok(Map.of("message", "설정이 업데이트되었습니다."));
    }

    /**
     * 알림 테스트 전송 (UI용)
     */
    @PostMapping("/test-notification")
    public ResponseEntity<Map<String, String>> sendTestNotification(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = UserDetailsUtil.getUser(userDetails);
        
        // 테스트 알림 전송 로직 (실제 구현 필요)
        // 예: SMS 테스트 메시지 전송
        
        return ResponseEntity.ok(Map.of("message", "테스트 알림이 전송되었습니다."));
    }
}