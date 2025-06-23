package com.side.football_project.domain.reservation.type;

public enum ReservationStatus {
    PENDING("승인 대기"),
    CONFIRMED("승인 완료"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}