package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.ReservationStatus;

public record ReservationCreateResponse(
        Long reservationId,
        String reservationCode,
        ReservationStatus status
) {
}