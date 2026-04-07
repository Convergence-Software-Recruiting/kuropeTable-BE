package com.example.kurope.domain.restaurant.repository;

import com.example.kurope.domain.restaurant.entity.Waiting;
import com.example.kurope.domain.restaurant.entity.WaitingStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    Optional<Waiting> findByWaitingCode(String waitingCode);

    List<Waiting> findByRestaurantIdOrderByWaitingNumberAsc(Long restaurantId);

    List<Waiting> findByRestaurantIdAndStatusOrderByWaitingNumberAsc(Long restaurantId, WaitingStatus status);

    long countByRestaurantIdAndStatusIn(Long restaurantId, Collection<WaitingStatus> statuses);

    long countByRestaurantIdAndStatusInAndWaitingNumberLessThan(
            Long restaurantId,
            Collection<WaitingStatus> statuses,
            int waitingNumber
    );

    @Query("""
        select coalesce(max(w.waitingNumber), 0)
        from Waiting w
        where w.restaurant.id = :restaurantId
          and w.registeredAt >= :start
          and w.registeredAt < :end
    """)
    int findMaxWaitingNumberForDay(
            @Param("restaurantId") Long restaurantId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}