package com.example.kurope.domain.restaurant.controller.admin;

import com.example.kurope.domain.restaurant.dto.ReservationAdminResponse;
import com.example.kurope.domain.restaurant.dto.ReservationStatusUpdateRequest;
import com.example.kurope.domain.restaurant.dto.WaitingAdminResponse;
import com.example.kurope.domain.restaurant.dto.WaitingSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Restaurant Admin", description = "관리자용 식당 예약/웨이팅 관리 API")
public interface RestaurantAdminControllerDocs {

    @Operation(
            summary = "식당 예약 목록 조회",
            description = "특정 식당의 예약 목록을 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "예약 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ReservationAdminResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    List<ReservationAdminResponse> getReservations(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId
    );

    @Operation(
            summary = "예약 상태 변경",
            description = "특정 예약의 상태를 변경한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 상태 변경 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음", content = @Content)
    })
    void changeReservationStatus(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long reservationId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "변경할 예약 상태 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationStatusUpdateRequest.class))
            )
            @Valid @RequestBody ReservationStatusUpdateRequest request
    );

    @Operation(
            summary = "웨이팅 요약 정보 조회",
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
            summary = "웨이팅 목록 조회",
            description = "특정 식당의 전체 웨이팅 목록을 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "웨이팅 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = WaitingAdminResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    List<WaitingAdminResponse> getWaitingList(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId
    );

    @Operation(
            summary = "다음 웨이팅 호출",
            description = "특정 식당에서 다음 순번의 웨이팅을 호출한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "다음 웨이팅 호출 성공",
                    content = @Content(schema = @Schema(implementation = WaitingAdminResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "호출 가능한 웨이팅이 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음", content = @Content)
    })
    WaitingAdminResponse callNextWaiting(
            @Parameter(description = "식당 ID", example = "1")
            @PathVariable Long restaurantId
    );

    @Operation(
            summary = "웨이팅 착석 처리",
            description = "특정 웨이팅을 착석 상태로 변경한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "착석 처리 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "착석 처리할 수 없는 상태", content = @Content),
            @ApiResponse(responseCode = "404", description = "웨이팅을 찾을 수 없음", content = @Content)
    })
    void seatWaiting(
            @Parameter(description = "웨이팅 ID", example = "1")
            @PathVariable Long waitingId
    );

    @Operation(
            summary = "웨이팅 취소 처리",
            description = "관리자가 특정 웨이팅을 취소 처리한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "웨이팅 취소 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "취소 처리할 수 없는 상태", content = @Content),
            @ApiResponse(responseCode = "404", description = "웨이팅을 찾을 수 없음", content = @Content)
    })
    void cancelWaiting(
            @Parameter(description = "웨이팅 ID", example = "1")
            @PathVariable Long waitingId
    );
}