package com.side.football_project.domain.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    // 새로운 예약 시스템용 필드
    private Long stadiumId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer totalPrice;
    private String memo;
    
    // 기존 시스템과의 호환성을 위한 필드
    private String name;
    private BigDecimal fee;
    private Long userId;

    public ReservationRequestDto(Long stadiumId, LocalDate reservationDate, 
                               LocalTime startTime, LocalTime endTime, 
                               Integer totalPrice, String memo) {
        this.stadiumId = stadiumId;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.memo = memo;
    }

    // 기존 코드와의 호환성을 위한 생성자 및 빌더
    @Builder
    public ReservationRequestDto(String name, BigDecimal fee, Long userId, Long stadiumId) {
        this.name = name;
        this.fee = fee;
        this.userId = userId;
        this.stadiumId = stadiumId;
    }

    // 기존 코드와의 호환성을 위한 메서드들
    public String getName() {
        return name;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public Long getUserId() {
        return userId;
    }
}
