package com.example.kurope.domain.restaurant.dto;

import com.example.kurope.domain.restaurant.entity.WaitingStatus;

public record MyWaitingStatusResponse(
        String waitingCode,
        WaitingStatus status,
        int waitingNumber,
        long aheadTeams,
        boolean onsiteVerified,
        boolean needOnsiteCheck,
        long estimatedWaitMinutes
) {
}