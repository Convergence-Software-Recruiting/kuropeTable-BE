package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationAdminResponse(
        Long reservationId,
        String reservationCode,
        String guestName,
        String guestPhone,
        LocalDate reservationDate,
        LocalTime reservationTime,
        int partySize,
        String requestNote,
        ReservationStatus status
) {
}