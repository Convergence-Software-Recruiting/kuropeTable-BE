package com.example.kurope.domain.restaurant.dto;

import jakarta.validation.constraints.NotBlank;

public record WaitingCancelRequest(
        @NotBlank String guestPhone
) {
}