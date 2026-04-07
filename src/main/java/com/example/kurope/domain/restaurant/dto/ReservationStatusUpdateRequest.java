package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record ReservationStatusUpdateRequest(
        @NotNull ReservationStatus status
) {
}