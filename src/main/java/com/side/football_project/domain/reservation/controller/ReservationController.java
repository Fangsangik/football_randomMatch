package com.side.football_project.domain.reservation.controller;

import com.side.football_project.domain.reservation.dto.ReservationDeleteRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.service.ReservationService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(reservationService.createReservation(requestDto, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> findReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findReservation(id));
    }

    @GetMapping("/stadiums/{id}")
    public ResponseEntity<Page<ReservationResponseDto>> findReservation(@PathVariable Long id,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponseDto> reservation = reservationService.findAllReservationByStadium(id, page, size);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/stadium/{id}")
    public ResponseEntity<ReservationResponseDto> findReservationByStadium(@RequestParam Long id) {
        return ResponseEntity.ok(reservationService.findReservationByStadium(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationResponseDto>> findReservationByUser(@PathVariable Long userId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponseDto> reservation = reservationService.findReservationByUser(userId, page, size);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@RequestBody ReservationDeleteRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        reservationService.deleteReservation(requestDto, user);
        return ResponseEntity.noContent().build();
    }
}
