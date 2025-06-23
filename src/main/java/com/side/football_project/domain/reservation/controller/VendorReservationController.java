package com.side.football_project.domain.reservation.controller;

import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.service.VendorReservationService;
import com.side.football_project.domain.reservation.service.VendorReservationService.ReservationStatsDto;
import com.side.football_project.domain.reservation.type.ReservationStatus;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.util.VendorDetailsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/vendor/reservations")
@RequiredArgsConstructor
public class VendorReservationController {

    private final VendorReservationService vendorReservationService;

    /**
     * Vendor의 모든 예약 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservations(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        log.info("Vendor {} 예약 조회 요청 (status: {}, 기간: {} ~ {})", vendor.getId(), status, startDate, endDate);
        
        List<ReservationResponseDto> reservations;
        
        if (status != null) {
            // 상태별 조회
            reservations = vendorReservationService.getReservationsByVendorAndStatus(vendor, status);
        } else if (startDate != null && endDate != null) {
            // 날짜 범위별 조회
            reservations = vendorReservationService.getReservationsByVendorAndDateRange(vendor, startDate, endDate);
        } else {
            // 전체 조회
            reservations = vendorReservationService.getReservationsByVendor(vendor);
        }
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * 예약 승인
     */
    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<Map<String, String>> confirmReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        log.info("예약 {} 승인 요청 (Vendor: {})", reservationId, vendor.getId());
        
        vendorReservationService.confirmReservation(reservationId, vendor);
        
        return ResponseEntity.ok(Map.of("message", "예약이 승인되었습니다."));
    }

    /**
     * 예약 취소
     */
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<Map<String, String>> cancelReservation(
            @PathVariable Long reservationId,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        String reason = request.getOrDefault("reason", "업체 사정으로 인한 취소");
        
        log.info("예약 {} 취소 요청 (Vendor: {}, 사유: {})", reservationId, vendor.getId(), reason);
        
        vendorReservationService.cancelReservation(reservationId, reason, vendor);
        
        return ResponseEntity.ok(Map.of("message", "예약이 취소되었습니다."));
    }

    /**
     * 예약 완료 처리
     */
    @PostMapping("/{reservationId}/complete")
    public ResponseEntity<Map<String, String>> completeReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        log.info("예약 {} 완료 처리 요청 (Vendor: {})", reservationId, vendor.getId());
        
        vendorReservationService.completeReservation(reservationId, vendor);
        
        return ResponseEntity.ok(Map.of("message", "예약이 완료 처리되었습니다."));
    }

    /**
     * 예약 통계 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<ReservationStatsDto> getReservationStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        log.info("Vendor {} 예약 통계 조회 요청", vendor.getId());
        
        ReservationStatsDto stats = vendorReservationService.getReservationStats(vendor);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * 오늘 예약 조회
     */
    @GetMapping("/today")
    public ResponseEntity<List<ReservationResponseDto>> getTodayReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        LocalDate today = LocalDate.now();
        
        log.info("Vendor {} 오늘({}) 예약 조회 요청", vendor.getId(), today);
        
        List<ReservationResponseDto> reservations = vendorReservationService
                .getReservationsByVendorAndDateRange(vendor, today, today);
        
        return ResponseEntity.ok(reservations);
    }

    /**
     * 대기 중인 예약 조회
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ReservationResponseDto>> getPendingReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Vendor vendor = VendorDetailsUtil.getVendor(userDetails);
        log.info("Vendor {} 대기 중인 예약 조회 요청", vendor.getId());
        
        List<ReservationResponseDto> reservations = vendorReservationService
                .getReservationsByVendorAndStatus(vendor, ReservationStatus.PENDING);
        
        return ResponseEntity.ok(reservations);
    }
}