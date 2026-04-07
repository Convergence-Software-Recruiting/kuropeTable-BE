package com.example.kurope.domain.restaurant.service;

import com.example.kurope.domain.restaurant.dto.RestaurantDetailResponse;
import com.example.kurope.domain.restaurant.entity.Restaurant;
import com.example.kurope.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantClientService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDetailResponse getRestaurantDetail(Long restaurantId) {
        Restaurant restaurant = getRestaurant(restaurantId);

        return new RestaurantDetailResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCountry(),
                restaurant.getCity(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getDescription(),
                restaurant.getOperatingStatus(),
                restaurant.isOpen(),
                restaurant.isReservationEnabled(),
                restaurant.isWaitingEnabled(),
                restaurant.isRemoteWaitingEnabled(),
                restaurant.getOnsiteCheckThreshold(),
                restaurant.getAvgDiningMinutes()
        );
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다."));
    }
}