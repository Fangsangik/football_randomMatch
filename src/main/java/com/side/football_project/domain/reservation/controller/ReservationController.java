package com.side.football_project.domain.reservation.controller;

import com.side.football_project.domain.reservation.dto.GetMyReservationResponseDto;
import com.side.football_project.domain.reservation.dto.ReservationDeleteRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationRequestDto;
import com.side.football_project.domain.reservation.dto.ReservationResponseDto;
import com.side.football_project.domain.reservation.service.UserReservationService;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final UserReservationService reservationService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(reservationService.createReservation(requestDto, user));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ReservationResponseDto> findReservation(@PathVariable Long id,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        return ResponseEntity.ok(reservationService.findReservation(id, user));
    }

    @GetMapping("/stadiums/{id}")
    @ResponseBody
    public ResponseEntity<Page<ReservationResponseDto>> findReservation(@PathVariable Long id,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponseDto> reservation = reservationService.findAllReservationByStadium(id, page, size);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/stadium/{id}")
    @ResponseBody
    public ResponseEntity<ReservationResponseDto> findReservationByStadium(@RequestParam Long id) {
        return ResponseEntity.ok(reservationService.findReservationByStadium(id));
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<Page<ReservationResponseDto>> findReservationByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        User user = UserDetailsUtil.getUser(userDetails);
        Page<ReservationResponseDto> reservation = reservationService.findReservationByUser(user.getId(), page, size);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@RequestBody ReservationDeleteRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = UserDetailsUtil.getUser(userDetails);
        reservationService.deleteReservation(requestDto, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my_reservations")
    @ResponseBody
    public ResponseEntity<Page<GetMyReservationResponseDto>> findMyReservations(@AuthenticationPrincipal UserDetails userDetails,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        User user = UserDetailsUtil.getUser(userDetails);
        Page<GetMyReservationResponseDto> myReservations = reservationService.findMyReservations(user, page, size);
        return ResponseEntity.ok(myReservations);
    }

}
