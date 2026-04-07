package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.WaitingRegisteredChannel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaitingRegisterRequest(
        @NotBlank String guestName,
        @NotBlank String guestPhone,
        @Min(1) int partySize,
        @NotNull WaitingRegisteredChannel registeredChannel
) {
}