package com.side.football_project.domain.stadium.controller;

import com.side.football_project.domain.reservation.dto.AvailableChekedReservationResponseDto;
import com.side.football_project.domain.reservation.service.UserReservationService;
import com.side.football_project.domain.stadium.dto.StadiumResponseDto;
import com.side.football_project.domain.stadium.service.StadiumService;
import com.side.football_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stadiums")
public class UserStadiumController {

    private final StadiumService stadiumService;
    private final UserReservationService reservationService;

    /**
     * 지역별 경기장 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<StadiumResponseDto>> searchStadiumsByRegion(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("경기장 검색 요청 - 지역: {}, 키워드: {}, 페이지: {}, 크기: {}", region, keyword, page, size);
        
        List<StadiumResponseDto> stadiums = stadiumService.searchStadiums(region, keyword, page, size);
        return ResponseEntity.ok(stadiums);
    }

    /**
     * 특정 경기장의 예약 가능한 시간대 조회
     */
    @GetMapping("/{stadiumId}/available-times")
    public ResponseEntity<Page<AvailableChekedReservationResponseDto>> getAvailableTimes(
            @PathVariable Long stadiumId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("경기장 {} 예약 가능 시간대 조회", stadiumId);
        
        Page<AvailableChekedReservationResponseDto> availableTimes = 
            reservationService.findAvailableReservations(stadiumId, page, size);
        
        return ResponseEntity.ok(availableTimes);
    }

    /**
     * 경기장 상세 정보 조회 (카카오 지도용 위치 정보 포함)
     */
    @GetMapping("/{stadiumId}")
    public ResponseEntity<StadiumResponseDto> getStadiumDetail(@PathVariable Long stadiumId) {
        log.info("경기장 {} 상세 정보 조회", stadiumId);
        
        StadiumResponseDto stadium = stadiumService.findById(stadiumId);
        return ResponseEntity.ok(stadium);
    }

    /**
     * 근처 경기장 조회 (위치 기반)
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<StadiumResponseDto>> getNearbyStadiums(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("근처 경기장 검색 - 위도: {}, 경도: {}, 반경: {}km", latitude, longitude, radiusKm);
        
        List<StadiumResponseDto> nearbyStadiums = 
            stadiumService.findNearbyStadiums(latitude, longitude, radiusKm, page, size);
        
        return ResponseEntity.ok(nearbyStadiums);
    }
}