package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.WaitingRegisteredChannel;
import com.example.kurope.domain.restaurant.entity.WaitingStatus;
import java.time.LocalDateTime;

public record WaitingAdminResponse(
        Long waitingId,
        String waitingCode,
        String guestName,
        String guestPhone,
        int partySize,
        int waitingNumber,
        WaitingRegisteredChannel registeredChannel,
        WaitingStatus status,
        boolean onsiteVerified,
        LocalDateTime registeredAt,
        LocalDateTime calledAt,
        LocalDateTime seatedAt,
        LocalDateTime canceledAt
) {
}