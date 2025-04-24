package com.side.football_project.domain.reservation.repository;

import com.side.football_project.global.common.exception.CustomException;
import com.side.football_project.global.common.exception.type.ReservationErrorCode;
import com.side.football_project.domain.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.stadium.id = :stadiumId ORDER BY r.createdAt DESC")
    Page<Reservation> findReservationByStadium(Long stadiumId, Pageable pageable);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.user where r.user.id = :userId ORDER BY r.createdAt DESC")
    Page<Reservation> findReservationByUser(Long userId, Pageable pageable);

    @Query("select r from Reservation r JOIN FETCH r.stadium where r.stadium.id = :stadiumId")
    Optional<Reservation> findReservationByStadiumId(Long stadiumId);


    default Reservation findReservationByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    @Query("SELECT r FROM Reservation r WHERE r.stadium.id = :stadiumId")
    List<Reservation> findAllByStadiumId(@Param("stadiumId") Long stadiumId);


}
