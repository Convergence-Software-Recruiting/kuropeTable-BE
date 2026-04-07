package com.example.kurope.domain.restaurant.service;

import com.example.kurope.domain.restaurant.dto.WaitingAdminResponse;
import com.example.kurope.domain.restaurant.dto.MyWaitingStatusResponse;
import com.example.kurope.domain.restaurant.dto.WaitingCancelRequest;
import com.example.kurope.domain.restaurant.dto.WaitingRegisterRequest;
import com.example.kurope.domain.restaurant.dto.WaitingRegisterResponse;
import com.example.kurope.domain.restaurant.dto.WaitingSummaryResponse;
import com.example.kurope.domain.restaurant.entity.Restaurant;
import com.example.kurope.domain.restaurant.entity.Waiting;
import com.example.kurope.domain.restaurant.entity.WaitingRegisteredChannel;
import com.example.kurope.domain.restaurant.entity.WaitingStatus;
import com.example.kurope.domain.restaurant.repository.WaitingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private static final List<WaitingStatus> ACTIVE_STATUSES = List.of(
            WaitingStatus.WAITING,
            WaitingStatus.CALLED
    );

    private final WaitingRepository waitingRepository;
    private final RestaurantClientService restaurantClientService;

    @Transactional
    public WaitingRegisterResponse registerWaiting(Long restaurantId, WaitingRegisterRequest request) {
        Restaurant restaurant = restaurantClientService.getRestaurant(restaurantId);
        restaurant.validateWaitingAvailable();

        if (request.registeredChannel() == WaitingRegisteredChannel.REMOTE) {
            restaurant.validateRemoteWaitingAvailable();
        }

        int nextWaitingNumber = getNextWaitingNumber(restaurantId);

        Waiting waiting = Waiting.register(
                restaurant,
                generateWaitingCode(),
                request.guestName(),
                request.guestPhone(),
                request.partySize(),
                nextWaitingNumber,
                request.registeredChannel()
        );

        waitingRepository.save(waiting);

        return new WaitingRegisterResponse(
                waiting.getId(),
                waiting.getWaitingCode(),
                waiting.getWaitingNumber(),
                waiting.getStatus()
        );
    }

    public WaitingSummaryResponse getWaitingSummary(Long restaurantId) {
        Restaurant restaurant = restaurantClientService.getRestaurant(restaurantId);

        long activeWaitingTeams = waitingRepository.countByRestaurantIdAndStatusIn(
                restaurantId,
                ACTIVE_STATUSES
        );

        long estimatedWaitMinutes = activeWaitingTeams * defaultAvgDiningMinutes(restaurant);

        return new WaitingSummaryResponse(
                restaurantId,
                activeWaitingTeams,
                estimatedWaitMinutes
        );
    }

    public MyWaitingStatusResponse getMyWaitingStatus(String waitingCode, String guestPhone) {
        Waiting waiting = waitingRepository.findByWaitingCode(waitingCode)
                .orElseThrow(() -> new IllegalArgumentException("웨이팅을 찾을 수 없습니다."));

        if (!waiting.getGuestPhone().equals(guestPhone)) {
            throw new IllegalArgumentException("전화번호가 일치하지 않습니다.");
        }

        long aheadTeams = 0;
        if (waiting.isActive()) {
            aheadTeams = waitingRepository.countByRestaurantIdAndStatusInAndWaitingNumberLessThan(
                    waiting.getRestaurant().getId(),
                    ACTIVE_STATUSES,
                    waiting.getWaitingNumber()
            );
        }

        boolean needOnsiteCheck = false;
        Integer threshold = waiting.getRestaurant().getOnsiteCheckThreshold();
        if (waiting.getRegisteredChannel() == WaitingRegisteredChannel.REMOTE
                && !waiting.isOnsiteVerified()
                && threshold != null
                && threshold > 0) {
            needOnsiteCheck = aheadTeams <= threshold;
        }

        long estimatedWaitMinutes = aheadTeams * defaultAvgDiningMinutes(waiting.getRestaurant());

        return new MyWaitingStatusResponse(
                waiting.getWaitingCode(),
                waiting.getStatus(),
                waiting.getWaitingNumber(),
                aheadTeams,
                waiting.isOnsiteVerified(),
                needOnsiteCheck,
                estimatedWaitMinutes
        );
    }

    @Transactional
    public void cancelMyWaiting(String waitingCode, WaitingCancelRequest request) {
        Waiting waiting = waitingRepository.findByWaitingCode(waitingCode)
                .orElseThrow(() -> new IllegalArgumentException("웨이팅을 찾을 수 없습니다."));

        if (!waiting.getGuestPhone().equals(request.guestPhone())) {
            throw new IllegalArgumentException("전화번호가 일치하지 않습니다.");
        }

        waiting.cancel();
    }

    public List<WaitingAdminResponse> getWaitingList(Long restaurantId) {
        restaurantClientService.getRestaurant(restaurantId);

        return waitingRepository.findByRestaurantIdOrderByWaitingNumberAsc(restaurantId)
                .stream()
                .map(waiting -> new WaitingAdminResponse(
                        waiting.getId(),
                        waiting.getWaitingCode(),
                        waiting.getGuestName(),
                        waiting.getGuestPhone(),
                        waiting.getPartySize(),
                        waiting.getWaitingNumber(),
                        waiting.getRegisteredChannel(),
                        waiting.getStatus(),
                        waiting.isOnsiteVerified(),
                        waiting.getRegisteredAt(),
                        waiting.getCalledAt(),
                        waiting.getSeatedAt(),
                        waiting.getCanceledAt()
                ))
                .toList();
    }

    @Transactional
    public WaitingAdminResponse callNextWaiting(Long restaurantId) {
        restaurantClientService.getRestaurant(restaurantId);

        Waiting waiting = waitingRepository.findByRestaurantIdAndStatusOrderByWaitingNumberAsc(
                        restaurantId,
                        WaitingStatus.WAITING
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("호출할 웨이팅이 없습니다."));

        waiting.call();

        return new WaitingAdminResponse(
                waiting.getId(),
                waiting.getWaitingCode(),
                waiting.getGuestName(),
                waiting.getGuestPhone(),
                waiting.getPartySize(),
                waiting.getWaitingNumber(),
                waiting.getRegisteredChannel(),
                waiting.getStatus(),
                waiting.isOnsiteVerified(),
                waiting.getRegisteredAt(),
                waiting.getCalledAt(),
                waiting.getSeatedAt(),
                waiting.getCanceledAt()
        );
    }

    @Transactional
    public void seatWaiting(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("웨이팅을 찾을 수 없습니다."));

        waiting.seat();
    }

    @Transactional
    public void cancelWaitingByAdmin(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("웨이팅을 찾을 수 없습니다."));

        waiting.cancel();
    }

    private int getNextWaitingNumber(Long restaurantId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return waitingRepository.findMaxWaitingNumberForDay(restaurantId, start, end) + 1;
    }

    private long defaultAvgDiningMinutes(Restaurant restaurant) {
        return restaurant.getAvgDiningMinutes() == null ? 30 : restaurant.getAvgDiningMinutes();
    }

    private String generateWaitingCode() {
        return "WT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}