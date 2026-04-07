package com.example.kurope.domain.restaurant.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateRequest(
        @NotBlank String guestName,
        @NotBlank String guestPhone,
        @NotNull @FutureOrPresent LocalDate reservationDate,
        @NotNull LocalTime reservationTime,
        @Min(1) int partySize,
        String requestNote
) {
}