package com.example.kurope.domain.restaurant.controller.client;

import com.example.kurope.domain.restaurant.dto.client.MyWaitingStatusResponse;
import com.example.kurope.domain.restaurant.dto.client.ReservationCreateRequest;
import com.example.kurope.domain.restaurant.dto.client.ReservationCreateResponse;
import com.example.kurope.domain.restaurant.dto.client.RestaurantDetailResponse;
import com.example.kurope.domain.restaurant.dto.client.WaitingCancelRequest;
import com.example.kurope.domain.restaurant.dto.client.WaitingRegisterRequest;
import com.example.kurope.domain.restaurant.dto.client.WaitingRegisterResponse;
import com.example.kurope.domain.restaurant.dto.client.WaitingSummaryResponse;
import com.example.kurope.domain.restaurant.service.ReservationService;
import com.example.kurope.domain.restaurant.service.RestaurantClientService;
import com.example.kurope.domain.restaurant.service.WaitingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantClientController {

    private final RestaurantClientService restaurantClientService;
    private final ReservationService reservationService;
    private final WaitingService waitingService;

    @GetMapping("/restaurants/{restaurantId}")
    public RestaurantDetailResponse getRestaurantDetail(@PathVariable Long restaurantId) {
        return restaurantClientService.getRestaurantDetail(restaurantId);
    }

    @PostMapping("/restaurants/{restaurantId}/reservations")
    public ReservationCreateResponse createReservation(
            @PathVariable Long restaurantId,
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        return reservationService.createReservation(restaurantId, request);
    }

    @GetMapping("/restaurants/{restaurantId}/waiting-summary")
    public WaitingSummaryResponse getWaitingSummary(@PathVariable Long restaurantId) {
        return waitingService.getWaitingSummary(restaurantId);
    }

    @PostMapping("/restaurants/{restaurantId}/waitings")
    public WaitingRegisterResponse registerWaiting(
            @PathVariable Long restaurantId,
            @Valid @RequestBody WaitingRegisterRequest request
    ) {
        return waitingService.registerWaiting(restaurantId, request);
    }

    @GetMapping("/waitings/{waitingCode}")
    public MyWaitingStatusResponse getMyWaitingStatus(
            @PathVariable String waitingCode,
            @RequestParam String guestPhone
    ) {
        return waitingService.getMyWaitingStatus(waitingCode, guestPhone);
    }

    @PostMapping("/waitings/{waitingCode}/cancel")
    public void cancelMyWaiting(
            @PathVariable String waitingCode,
            @Valid @RequestBody WaitingCancelRequest request
    ) {
        waitingService.cancelMyWaiting(waitingCode, request);
    }
}