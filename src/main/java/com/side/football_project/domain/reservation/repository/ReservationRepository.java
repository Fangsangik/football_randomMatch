package com.side.football_project.domain.reservation.repository;

import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.domain.reservation.type.ReservationStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.vendor.entity.Vendor;
import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 기존 메서드들
    @Query("SELECT r FROM Reservation r WHERE r.stadium.id = :stadiumId ORDER BY r.createdAt DESC")
    Page<Reservation> findReservationByStadium(Long stadiumId, Pageable pageable);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.user where r.user.id = :userId ORDER BY r.createdAt DESC")
    Page<Reservation> findReservationByUser(Long userId, Pageable pageable);

    @Query("select r from Reservation r JOIN FETCH r.stadium where r.stadium.id = :stadiumId")
    Optional<Reservation> findReservationByStadiumId(Long stadiumId);

    @Query("SELECT r FROM Reservation r WHERE r.stadium.id = :stadiumId")
    List<Reservation> findAllByStadiumId(@Param("stadiumId") Long stadiumId);

    // Vendor용 새로운 메서드들
    @Query("SELECT r FROM Reservation r WHERE r.stadium.vendor = :vendor ORDER BY r.createdAt DESC")
    List<Reservation> findByStadiumVendorOrderByCreatedAtDesc(@Param("vendor") Vendor vendor);

    @Query("SELECT r FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.status = :status ORDER BY r.createdAt DESC")
    List<Reservation> findByStadiumVendorAndStatusOrderByCreatedAtDesc(@Param("vendor") Vendor vendor, @Param("status") ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.reservationDate = :date ORDER BY r.startTime")
    List<Reservation> findByStadiumVendorAndReservationDate(@Param("vendor") Vendor vendor, @Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.reservationDate BETWEEN :startDate AND :endDate ORDER BY r.reservationDate, r.startTime")
    List<Reservation> findByStadiumVendorAndReservationDateBetween(
        @Param("vendor") Vendor vendor, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );

    // 특정 경기장의 예약 조회
    List<Reservation> findByStadiumOrderByCreatedAtDesc(Stadium stadium);

    // 특정 경기장의 특정 날짜 예약 조회
    List<Reservation> findByStadiumAndReservationDateOrderByStartTime(Stadium stadium, LocalDate reservationDate);

    // 사용자의 예약 조회
    List<Reservation> findByUserOrderByCreatedAtDesc(User user);

    // 시간 겹침 확인 (예약 충돌 방지)
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.stadium = :stadium AND r.reservationDate = :date " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.startTime < :endTime AND r.endTime > :startTime))")
    boolean existsConflictingReservation(
        @Param("stadium") Stadium stadium,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );

    // 통계용 쿼리들
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.reservationDate = :date")
    Long countByStadiumVendorAndReservationDate(@Param("vendor") Vendor vendor, @Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.reservationDate BETWEEN :startDate AND :endDate")
    Long countByStadiumVendorAndReservationDateBetween(
        @Param("vendor") Vendor vendor, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.stadium.vendor = :vendor AND r.status = :status")
    Long countByStadiumVendorAndStatus(@Param("vendor") Vendor vendor, @Param("status") ReservationStatus status);

    // 예약 ID로 조회 (에러 처리 포함)
    default Reservation findReservationByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    default Reservation findByIdOrElseThrow(Long reservationId) {
        return findById(reservationId).orElseThrow(() -> 
            new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :id ORDER BY r.createdAt DESC")
    Page<Reservation> findReservationsByUserId(Long id, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.stadium.id = :stadiumId AND r.status = 'PENDING' ORDER BY r.reservationDate, r.startTime")
    Page<Reservation> findAvailableReservationsByStadium(Long stadiumId, Pageable pageable);
}
