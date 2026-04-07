package com.example.kurope.domain.restaurant.controller.admin;

import com.example.kurope.domain.restaurant.dto.admin.ReservationAdminResponse;
import com.example.kurope.domain.restaurant.dto.admin.ReservationStatusUpdateRequest;
import com.example.kurope.domain.restaurant.dto.admin.WaitingAdminResponse;
import com.example.kurope.domain.restaurant.dto.client.WaitingSummaryResponse;
import com.example.kurope.domain.restaurant.service.ReservationService;
import com.example.kurope.domain.restaurant.service.WaitingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class RestaurantAdminController {

    private final ReservationService reservationService;
    private final WaitingService waitingService;

    @GetMapping("/restaurants/{restaurantId}/reservations")
    public List<ReservationAdminResponse> getReservations(@PathVariable Long restaurantId) {
        return reservationService.getReservations(restaurantId);
    }

    @PatchMapping("/reservations/{reservationId}/status")
    public void changeReservationStatus(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationStatusUpdateRequest request
    ) {
        reservationService.changeReservationStatus(reservationId, request);
    }

    @GetMapping("/restaurants/{restaurantId}/waiting-summary")
    public WaitingSummaryResponse getWaitingSummary(@PathVariable Long restaurantId) {
        return waitingService.getWaitingSummary(restaurantId);
    }

    @GetMapping("/restaurants/{restaurantId}/waitings")
    public List<WaitingAdminResponse> getWaitingList(@PathVariable Long restaurantId) {
        return waitingService.getWaitingList(restaurantId);
    }

    @PostMapping("/restaurants/{restaurantId}/waitings/call-next")
    public WaitingAdminResponse callNextWaiting(@PathVariable Long restaurantId) {
        return waitingService.callNextWaiting(restaurantId);
    }

    @PostMapping("/waitings/{waitingId}/seat")
    public void seatWaiting(@PathVariable Long waitingId) {
        waitingService.seatWaiting(waitingId);
    }

    @PostMapping("/waitings/{waitingId}/cancel")
    public void cancelWaiting(@PathVariable Long waitingId) {
        waitingService.cancelWaitingByAdmin(waitingId);
    }
}