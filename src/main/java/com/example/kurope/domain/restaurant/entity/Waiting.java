package com.example.kurope.domain.restaurant.entity;

import com.example.kurope.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "waiting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Column(name = "waiting_code", nullable = false, unique = true, length = 30)
    private String waitingCode;

    @Column(name = "guest_name", nullable = false, length = 50)
    private String guestName;

    @Column(name = "guest_phone", nullable = false, length = 30)
    private String guestPhone;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    @Column(name = "waiting_number", nullable = false)
    private int waitingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "registered_channel", nullable = false, length = 20)
    private WaitingRegisteredChannel registeredChannel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WaitingStatus status;

    @Column(name = "onsite_verified", nullable = false)
    private boolean onsiteVerified;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "onsite_verified_at")
    private LocalDateTime onsiteVerifiedAt;

    @Column(name = "called_at")
    private LocalDateTime calledAt;

    @Column(name = "seated_at")
    private LocalDateTime seatedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    private Waiting(
            Restaurant restaurant,
            String waitingCode,
            String guestName,
            String guestPhone,
            int partySize,
            int waitingNumber,
            WaitingRegisteredChannel registeredChannel
    ) {
        this.restaurant = restaurant;
        this.waitingCode = waitingCode;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.partySize = partySize;
        this.waitingNumber = waitingNumber;
        this.registeredChannel = registeredChannel;
        this.status = WaitingStatus.WAITING;
        this.onsiteVerified = registeredChannel == WaitingRegisteredChannel.ONSITE;
        this.registeredAt = LocalDateTime.now();
        this.onsiteVerifiedAt = this.onsiteVerified ? LocalDateTime.now() : null;
    }

    public static Waiting register(
            Restaurant restaurant,
            String waitingCode,
            String guestName,
            String guestPhone,
            int partySize,
            int waitingNumber,
            WaitingRegisteredChannel registeredChannel
    ) {
        return new Waiting(
                restaurant,
                waitingCode,
                guestName,
                guestPhone,
                partySize,
                waitingNumber,
                registeredChannel
        );
    }

    public void verifyOnsite() {
        if (this.onsiteVerified) {
            return;
        }
        this.onsiteVerified = true;
        this.onsiteVerifiedAt = LocalDateTime.now();
    }

    public void call() {
        if (this.status != WaitingStatus.WAITING) {
            throw new IllegalStateException("대기중 상태만 호출할 수 있습니다.");
        }
        this.status = WaitingStatus.CALLED;
        this.calledAt = LocalDateTime.now();
    }

    public void seat() {
        if (this.status != WaitingStatus.CALLED && this.status != WaitingStatus.WAITING) {
            throw new IllegalStateException("입장 처리할 수 없는 상태입니다.");
        }
        this.status = WaitingStatus.SEATED;
        this.seatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == WaitingStatus.SEATED || this.status == WaitingStatus.CANCELED) {
            throw new IllegalStateException("취소할 수 없는 상태입니다.");
        }
        this.status = WaitingStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == WaitingStatus.WAITING || this.status == WaitingStatus.CALLED;
    }
}