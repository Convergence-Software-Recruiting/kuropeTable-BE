package com.example.kurope.domain.restaurant.controller.client;

import com.example.kurope.domain.restaurant.dto.MyWaitingStatusResponse;
import com.example.kurope.domain.restaurant.dto.ReservationCreateRequest;
import com.example.kurope.domain.restaurant.dto.ReservationCreateResponse;
import com.example.kurope.domain.restaurant.dto.RestaurantDetailResponse;
import com.example.kurope.domain.restaurant.dto.WaitingCancelRequest;
import com.example.kurope.domain.restaurant.dto.WaitingRegisterRequest;
import com.example.kurope.domain.restaurant.dto.WaitingRegisterResponse;
import com.example.kurope.domain.restaurant.dto.WaitingSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Restaurant Client", description = "사용자용 식당 상세/예약/웨이팅 API")
public interface RestaurantClientControllerDocs {

    @Operation(
            summary = "식당 상세 조회",
            description = "특정 식당의 상세 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "식당 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = RestaurantDetailResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    RestaurantDetailResponse getRestaurantDetail(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId
    );

    @Operation(
            summary = "예약 생성",
            description = "특정 식당에 예약을 생성한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "예약 생성 성공",
                    content = @Content(schema = @Schema(implementation = ReservationCreateResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    ReservationCreateResponse createReservation(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "예약 생성 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationCreateRequest.class))
            )
            @Valid @RequestBody ReservationCreateRequest request
    );

    @Operation(
            summary = "웨이팅 요약 조회",
            description = "특정 식당의 현재 웨이팅 현황 요약 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "웨이팅 요약 조회 성공",
                    content = @Content(schema = @Schema(implementation = WaitingSummaryResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    WaitingSummaryResponse getWaitingSummary(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId
    );

    @Operation(
            summary = "웨이팅 등록",
            description = "특정 식당에 웨이팅을 등록한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "웨이팅 등록 성공",
                    content = @Content(schema = @Schema(implementation = WaitingRegisterResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    WaitingRegisterResponse registerWaiting(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "웨이팅 등록 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WaitingRegisterRequest.class))
            )
            @Valid @RequestBody WaitingRegisterRequest request
    );

    @Operation(
            summary = "내 웨이팅 상태 조회",
            description = "웨이팅 코드와 전화번호로 내 웨이팅 상태를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 웨이팅 상태 조회 성공",
                    content = @Content(schema = @Schema(implementation = MyWaitingStatusResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content),
            @ApiResponse(responseCode = "404", description = "웨이팅을 찾을 수 없음", content = @Content)
    })
    MyWaitingStatusResponse getMyWaitingStatus(
            @Parameter(description = "웨이팅 코드", example = "WT-20260409-001")
            @PathVariable String waitingCode,

            @Parameter(description = "손님 전화번호", example = "01012345678")
            @RequestParam String guestPhone
    );

    @Operation(
            summary = "내 웨이팅 취소",
            description = "웨이팅 코드와 요청 정보를 기반으로 내 웨이팅을 취소한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "웨이팅 취소 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청값 또는 취소 불가능한 상태", content = @Content),
            @ApiResponse(responseCode = "404", description = "웨이팅을 찾을 수 없음", content = @Content)
    })
    void cancelMyWaiting(
            @Parameter(description = "웨이팅 코드", example = "WT-20260409-001")
            @PathVariable String waitingCode,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "웨이팅 취소 요청 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WaitingCancelRequest.class))
            )
            @Valid @RequestBody WaitingCancelRequest request
    );
}