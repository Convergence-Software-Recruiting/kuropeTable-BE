package com.example.kurope.domain.restaurant.dto;

public record WaitingSummaryResponse(
        Long restaurantId,
        long activeWaitingTeams,
        long estimatedWaitMinutes
) {
}