package com.side.football_project.domain.reservation.service;

import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.domain.reservation.repository.ReservationRepository;
import com.side.football_project.domain.reservation.type.ReservationStatus;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorReservationServiceImpl implements VendorReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationResponseDto> getReservationsByVendor(Vendor vendor) {
        log.info("Vendor {}의 모든 예약 조회", vendor.getId());
        
        List<Reservation> reservations = reservationRepository.findByStadiumVendorOrderByCreatedAtDesc(vendor);
        
        return reservations.stream()
                .map(ReservationResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getReservationsByVendorAndStatus(Vendor vendor, ReservationStatus status) {
        log.info("Vendor {}의 상태 {} 예약 조회", vendor.getId(), status);
        
        List<Reservation> reservations = reservationRepository
                .findByStadiumVendorAndStatusOrderByCreatedAtDesc(vendor, status);
        
        return reservations.stream()
                .map(ReservationResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getReservationsByVendorAndDateRange(Vendor vendor, LocalDate startDate, LocalDate endDate) {
        log.info("Vendor {}의 날짜 범위 ({} ~ {}) 예약 조회", vendor.getId(), startDate, endDate);
        
        List<Reservation> reservations = reservationRepository
                .findByStadiumVendorAndReservationDateBetween(vendor, startDate, endDate);
        
        return reservations.stream()
                .map(ReservationResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void confirmReservation(Long reservationId, Vendor vendor) {
        log.info("예약 {} 승인 처리 (Vendor: {})", reservationId, vendor.getId());
        
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        
        // Vendor 소유 경기장인지 확인
        validateVendorOwnership(reservation, vendor);
        
        // 예약 승인
        reservation.confirm();
        
        reservationRepository.save(reservation);
        log.info("예약 {} 승인 완료", reservationId);
    }

    @Transactional
    @Override
    public void cancelReservation(Long reservationId, String reason, Vendor vendor) {
        log.info("예약 {} 취소 처리 (Vendor: {}, 사유: {})", reservationId, vendor.getId(), reason);

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        // Vendor 소유 경기장인지 확인
        validateVendorOwnership(reservation, vendor);

        // 예약 취소
        reservation.cancel(reason);

        reservationRepository.save(reservation);
        log.info("예약 {} 취소 완료", reservationId);
    }

    @Transactional
    @Override
    public void completeReservation(Long reservationId, Vendor vendor) {
        log.info("예약 {} 완료 처리 (Vendor: {})", reservationId, vendor.getId());

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        // Vendor 소유 경기장인지 확인
        validateVendorOwnership(reservation, vendor);

        // 예약 완료
        reservation.complete();

        reservationRepository.save(reservation);
        log.info("예약 {} 완료 처리 완료", reservationId);
    }

    @Override
    public ReservationStatsDto getReservationStats(Vendor vendor) {
        log.info("Vendor {}의 예약 통계 조회", vendor.getId());
        
        LocalDate today = LocalDate.now();
        
        // 이번 주 시작일과 종료일 계산
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = today.with(weekFields.dayOfWeek(), 1);
        LocalDate endOfWeek = today.with(weekFields.dayOfWeek(), 7);
        
        // 각 통계 조회
        Long todayReservations = reservationRepository
                .countByStadiumVendorAndReservationDate(vendor, today);
        
        Long weekReservations = reservationRepository
                .countByStadiumVendorAndReservationDateBetween(vendor, startOfWeek, endOfWeek);
        
        Long pendingReservations = reservationRepository
                .countByStadiumVendorAndStatus(vendor, ReservationStatus.PENDING);
        
        Long completedReservations = reservationRepository
                .countByStadiumVendorAndStatus(vendor, ReservationStatus.COMPLETED);
        
        return new ReservationStatsDto(
                todayReservations,
                weekReservations, 
                pendingReservations,
                completedReservations
        );
    }

    /**
     * Vendor가 해당 예약의 경기장을 소유하고 있는지 확인
     */
    private void validateVendorOwnership(Reservation reservation, Vendor vendor) {
        if (!reservation.getStadium().getVendor().getId().equals(vendor.getId())) {
            log.error("Vendor {}가 예약 {}의 경기장을 소유하지 않음", vendor.getId(), reservation.getId());
            throw new CustomException(ReservationErrorCode.RESERVATION_ACCESS_DENIED);
        }
    }
}