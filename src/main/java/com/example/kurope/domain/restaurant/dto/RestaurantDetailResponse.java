package com.example.kurope.domain.restaurant.dto.client;

import com.example.kurope.domain.restaurant.entity.OperatingStatus;

public record RestaurantDetailResponse(
        Long id,
        String name,
        String country,
        String city,
        String address,
        String phone,
        String description,
        OperatingStatus operatingStatus,
        boolean currentlyAvailable,
        boolean reservationEnabled,
        boolean waitingEnabled,
        boolean remoteWaitingEnabled,
        Integer onsiteCheckThreshold,
        Integer avgDiningMinutes
) {
}