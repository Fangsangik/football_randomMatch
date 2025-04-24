package com.side.football_project.domain.stadium.entity;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Stadium extends BaseEntity {

    //최대 수용 인원 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private StadiumStatus status;

    private String description;

    private Integer capacity;
    private Integer currentReservationCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Embedded
    private Address address;

    @Version
    private Long version;

    @Builder
    public Stadium(String name, StadiumStatus status, String description, Integer capacity, Integer currentReservationCount, User user, Address address, Long version) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.capacity = capacity;
        this.currentReservationCount = (currentReservationCount != null) ? currentReservationCount : 0;
        this.user = user;
        this.address = address;
        this.version = version;
    }

    public void applyAddress(Address address) {
        this.address = address;
    }

    public boolean isFullyBooked() {
        return this.currentReservationCount >= this.capacity;
    }

    public void increaseCurrentReservationCount() {
        if (isFullyBooked()) {
            throw new IllegalStateException("이미 예약이 꽉 찼습니다.");
        }

        this.currentReservationCount++;
    }

    public void decreaseCurrentReservationCount() {
        if (this.currentReservationCount > 0) {
            this.currentReservationCount--;
        }
    }
}
