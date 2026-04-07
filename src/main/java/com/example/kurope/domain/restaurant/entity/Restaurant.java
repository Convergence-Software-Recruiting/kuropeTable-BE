package com.example.kurope.domain.restaurant.entity;

import com.example.kurope.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "operating_status", nullable = false, length = 30)
    private OperatingStatus operatingStatus;

    @Column(name = "reservation_enabled", nullable = false)
    private boolean reservationEnabled;

    @Column(name = "waiting_enabled", nullable = false)
    private boolean waitingEnabled;

    @Column(name = "remote_waiting_enabled", nullable = false)
    private boolean remoteWaitingEnabled;

    @Column(name = "onsite_check_threshold")
    private Integer onsiteCheckThreshold;

    @Column(name = "avg_dining_minutes")
    private Integer avgDiningMinutes;

    public boolean isOpen() {
        return operatingStatus == OperatingStatus.OPEN;
    }

    public void validateReservable() {
        if (!reservationEnabled) {
            throw new IllegalStateException("해당 식당은 예약 기능을 사용하지 않습니다.");
        }
        if (!isOpen()) {
            throw new IllegalStateException("현재 예약 가능한 상태가 아닙니다.");
        }
    }

    public void validateWaitingAvailable() {
        if (!waitingEnabled) {
            throw new IllegalStateException("해당 식당은 웨이팅 기능을 사용하지 않습니다.");
        }
        if (!isOpen()) {
            throw new IllegalStateException("현재 웨이팅 가능한 상태가 아닙니다.");
        }
    }

    public void validateRemoteWaitingAvailable() {
        if (!remoteWaitingEnabled) {
            throw new IllegalStateException("해당 식당은 원격 웨이팅을 지원하지 않습니다.");
        }
    }
}