package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.type.ReservationStatus;
import com.side.football_project.domain.vendor.entity.Vendor;

import java.time.LocalDate;
import java.util.List;

public interface VendorReservationService {
    
    /**
     * Vendor의 모든 예약 조회
     */
    List<ReservationResponseDto> getReservationsByVendor(Vendor vendor);
    
    /**
     * Vendor의 특정 상태 예약 조회
     */
    List<ReservationResponseDto> getReservationsByVendorAndStatus(Vendor vendor, ReservationStatus status);
    
    /**
     * Vendor의 날짜 범위 예약 조회
     */
    List<ReservationResponseDto> getReservationsByVendorAndDateRange(Vendor vendor, LocalDate startDate, LocalDate endDate);
    
    /**
     * 예약 승인
     */
    void confirmReservation(Long reservationId, Vendor vendor);
    
    /**
     * 예약 취소
     */
    void cancelReservation(Long reservationId, String reason, Vendor vendor);
    
    /**
     * 예약 완료 처리
     */
    void completeReservation(Long reservationId, Vendor vendor);
    
    /**
     * Vendor의 예약 통계 조회
     */
    ReservationStatsDto getReservationStats(Vendor vendor);
    
    /**
     * 예약 통계 DTO
     */
    class ReservationStatsDto {
        private final Long todayReservations;
        private final Long weekReservations;
        private final Long pendingReservations;
        private final Long completedReservations;
        
        public ReservationStatsDto(Long todayReservations, Long weekReservations, 
                                 Long pendingReservations, Long completedReservations) {
            this.todayReservations = todayReservations;
            this.weekReservations = weekReservations;
            this.pendingReservations = pendingReservations;
            this.completedReservations = completedReservations;
        }
        
        public Long getTodayReservations() { return todayReservations; }
        public Long getWeekReservations() { return weekReservations; }
        public Long getPendingReservations() { return pendingReservations; }
        public Long getCompletedReservations() { return completedReservations; }
    }
}