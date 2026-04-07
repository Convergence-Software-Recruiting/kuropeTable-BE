package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.WaitingStatus;

public record WaitingRegisterResponse(
        Long waitingId,
        String waitingCode,
        int waitingNumber,
        WaitingStatus status
) {
}