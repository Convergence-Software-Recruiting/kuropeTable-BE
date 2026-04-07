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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Column(name = "reservation_code", nullable = false, unique = true, length = 30)
    private String reservationCode;

    @Column(name = "guest_name", nullable = false, length = 50)
    private String guestName;

    @Column(name = "guest_phone", nullable = false, length = 30)
    private String guestPhone;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservation_time", nullable = false)
    private LocalTime reservationTime;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    @Column(name = "request_note", length = 255)
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReservationStatus status;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    private Reservation(
            Restaurant restaurant,
            String reservationCode,
            String guestName,
            String guestPhone,
            LocalDate reservationDate,
            LocalTime reservationTime,
            int partySize,
            String requestNote
    ) {
        this.restaurant = restaurant;
        this.reservationCode = reservationCode;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.partySize = partySize;
        this.requestNote = requestNote;
        this.status = ReservationStatus.REQUESTED;
    }

    public static Reservation create(
            Restaurant restaurant,
            String reservationCode,
            String guestName,
            String guestPhone,
            LocalDate reservationDate,
            LocalTime reservationTime,
            int partySize,
            String requestNote
    ) {
        return new Reservation(
                restaurant,
                reservationCode,
                guestName,
                guestPhone,
                reservationDate,
                reservationTime,
                partySize,
                requestNote
        );
    }

    public void changeStatus(ReservationStatus status) {
        this.status = status;
        if (status == ReservationStatus.CANCELED) {
            this.canceledAt = LocalDateTime.now();
        }
    }
}