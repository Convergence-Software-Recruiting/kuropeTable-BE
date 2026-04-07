package com.example.kurope.domain.restaurant.service;

import com.example.kurope.domain.restaurant.dto.ReservationAdminResponse;
import com.example.kurope.domain.restaurant.dto.ReservationStatusUpdateRequest;
import com.example.kurope.domain.restaurant.dto.ReservationCreateRequest;
import com.example.kurope.domain.restaurant.dto.ReservationCreateResponse;
import com.example.kurope.domain.restaurant.entity.Reservation;
import com.example.kurope.domain.restaurant.entity.Restaurant;
import com.example.kurope.domain.restaurant.repository.ReservationRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantClientService restaurantClientService;

    @Transactional
    public ReservationCreateResponse createReservation(Long restaurantId, ReservationCreateRequest request) {
        Restaurant restaurant = restaurantClientService.getRestaurant(restaurantId);
        restaurant.validateReservable();

        Reservation reservation = Reservation.create(
                restaurant,
                generateReservationCode(),
                request.guestName(),
                request.guestPhone(),
                request.reservationDate(),
                request.reservationTime(),
                request.partySize(),
                request.requestNote()
        );

        reservationRepository.save(reservation);

        return new ReservationCreateResponse(
                reservation.getId(),
                reservation.getReservationCode(),
                reservation.getStatus()
        );
    }

    public List<ReservationAdminResponse> getReservations(Long restaurantId) {
        restaurantClientService.getRestaurant(restaurantId);

        return reservationRepository.findByRestaurantIdOrderByReservationDateAscReservationTimeAsc(restaurantId)
                .stream()
                .map(reservation -> new ReservationAdminResponse(
                        reservation.getId(),
                        reservation.getReservationCode(),
                        reservation.getGuestName(),
                        reservation.getGuestPhone(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getPartySize(),
                        reservation.getRequestNote(),
                        reservation.getStatus()
                ))
                .toList();
    }

    @Transactional
    public void changeReservationStatus(Long reservationId, ReservationStatusUpdateRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        reservation.changeStatus(request.status());
    }

    private String generateReservationCode() {
        return "RSV-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}