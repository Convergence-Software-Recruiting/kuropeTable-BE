package com.example.kurope.domain.restaurant.repository;

import com.example.kurope.domain.restaurant.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRestaurantIdOrderByReservationDateAscReservationTimeAsc(Long restaurantId);
}