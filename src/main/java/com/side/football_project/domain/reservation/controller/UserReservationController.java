package com.side.football_project.domain.reservation.controller;

import com.side.football_project.domain.reservation.dto.*;
import com.side.football_project.domain.reservation.service.UserReservationService;
import com.side.football_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class UserReservationController {

    private final UserReservationService reservationService;

    /**
     * 예약 신청 (사용자 → Vendor 승인 대기)
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody CreateReservationRequestDto requestDto) {
        
        // TODO: JWT 토큰에서 사용자 정보 추출 (임시로 null 사용)
        User user = null;
        
        log.info("사용자 {} 예약 신청 - 경기장: {}, 날짜: {}, 시간: {}-{}", 
                user.getId(), requestDto.getStadiumId(), requestDto.getReservationDate(), 
                requestDto.getStartTime(), requestDto.getEndTime());
        
        ReservationResponseDto reservation = reservationService.createUserReservation(requestDto, user);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 내 예약 목록 조회
     */
    @GetMapping("/my")
    public ResponseEntity<Page<GetMyReservationResponseDto>> getMyReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // TODO: JWT 토큰에서 사용자 정보 추출
        User user = null;
        
        log.info("사용자 {} 예약 목록 조회", user.getId());
        
        Page<GetMyReservationResponseDto> reservations = 
            reservationService.findMyReservations(user, page, size);
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * 예약 상세 조회
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDto> getReservationDetail(
            @PathVariable Long reservationId) {
        
        // TODO: JWT 토큰에서 사용자 정보 추출
        User user = null;
        
        log.info("사용자 {} 예약 {} 상세 조회", user.getId(), reservationId);
        
        ReservationResponseDto reservation = reservationService.findReservation(reservationId, user);
        return ResponseEntity.ok(reservation);
    }

    /**
     * 예약 취소 (사용자)
     */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long reservationId,
            @RequestBody(required = false) CancelReservationRequestDto requestDto) {
        
        // TODO: JWT 토큰에서 사용자 정보 추출
        User user = null;
        
        String reason = requestDto != null ? requestDto.getReason() : "사용자 취소";
        log.info("사용자 {} 예약 {} 취소 - 사유: {}", user.getId(), reservationId, reason);
        
        reservationService.cancelUserReservation(reservationId, reason, user);
        return ResponseEntity.ok().build();
    }
}