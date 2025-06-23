package com.side.football_project.domain.reservation.entity;

import com.side.football_project.domain.reservation.type.ReservationStatus;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(length = 500)
    private String memo;

    @Column(length = 500)
    private String cancelReason;

    @Builder
    public Reservation(Stadium stadium, User user, LocalDate reservationDate, 
                      LocalTime startTime, LocalTime endTime, Integer totalPrice, String memo,
                      String name, java.math.BigDecimal fee) {
        this.stadium = stadium;
        this.user = user;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice != null ? totalPrice : (fee != null ? fee.intValue() : 0);
        this.memo = memo;
        this.status = ReservationStatus.PENDING; // 기본값은 승인 대기
        // name 파라미터는 무시 (새 시스템에서는 ID 기반으로 자동 생성)
    }

    // 예약 승인
    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태인 예약만 승인할 수 있습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    // 예약 취소
    public void cancel(String reason) {
        if (this.status == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("완료된 예약은 취소할 수 없습니다.");
        }
        this.status = ReservationStatus.CANCELLED;
        this.cancelReason = reason;
    }

    // 예약 완료 처리
    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("승인된 예약만 완료 처리할 수 있습니다.");
        }
        this.status = ReservationStatus.COMPLETED;
    }

    // 예약 시간 검증
    public boolean isValidReservationTime() {
        return startTime.isBefore(endTime);
    }

    // 예약 날짜가 과거인지 확인
    public boolean isPastReservation() {
        return reservationDate.isBefore(LocalDate.now());
    }

    // 기존 코드와의 호환성을 위한 메서드들 (임시)
    public String getName() {
        return "예약-" + id; // 임시 이름
    }

    public java.math.BigDecimal getFee() {
        return java.math.BigDecimal.valueOf(totalPrice != null ? totalPrice : 0);
    }

    public void addTeam(Object team) {
        // 기존 코드에서 team을 추가하려고 할 때 - 새 시스템에서는 사용하지 않음
        // 빈 구현으로 컴파일 에러만 방지
    }
}
