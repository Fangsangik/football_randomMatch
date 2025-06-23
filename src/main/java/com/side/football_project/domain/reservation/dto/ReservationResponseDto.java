package com.side.football_project.domain.reservation.dto;

import com.side.football_project.domain.reservation.entity.Reservation;
import com.side.football_project.domain.reservation.type.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationResponseDto {
    private Long id;
    private Long stadiumId;
    private String stadiumName;
    private Long userId;
    private String customerName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;
    private Integer totalPrice;
    private String memo;
    private String cancelReason;
    private LocalDateTime createdAt;

    public ReservationResponseDto(Long id, Long stadiumId, String stadiumName, 
                                Long userId, String customerName,
                                LocalDate reservationDate, LocalTime startTime, LocalTime endTime,
                                ReservationStatus status, Integer totalPrice, String memo, 
                                String cancelReason, LocalDateTime createdAt) {
        this.id = id;
        this.stadiumId = stadiumId;
        this.stadiumName = stadiumName;
        this.userId = userId;
        this.customerName = customerName;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.memo = memo;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt;
    }

    public static ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(
            reservation.getId(),
            reservation.getStadium().getId(),
            reservation.getStadium().getName(),
            reservation.getUser().getId(),
            reservation.getUser().getName(),
            reservation.getReservationDate(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getStatus(),
            reservation.getTotalPrice(),
            reservation.getMemo(),
            reservation.getCancelReason(),
            reservation.getCreatedAt()
        );
    }

    // 기존 코드와의 호환성을 위한 메서드들
    public static ReservationResponseDto toEntity(Reservation reservation) {
        return toDto(reservation); // 새로운 toDto 메서드를 호출
    }
    
    // DTO를 Entity로 변환하는 메서드 (Match 빌더 호환용)
    public static Reservation toReservationEntity(ReservationResponseDto dto) {
        return Reservation.builder()
                .stadium(null) // Match에서 stadium을 직접 설정
                .user(null) // Match에서 user를 직접 설정
                .reservationDate(dto.getReservationDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .totalPrice(dto.getTotalPrice())
                .memo(dto.getMemo())
                .build();
    }

    // 기존 코드에서 사용하는 getStadium() 메서드 (임시 호환용)
    public com.side.football_project.domain.stadium.entity.Stadium getStadium() {
        // Mock Stadium 객체 생성 (컴파일 에러 방지용)
        return com.side.football_project.domain.stadium.entity.Stadium.builder()
                .name(stadiumName)
                .build();
    }
}
