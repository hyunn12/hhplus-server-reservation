package io.hhplus.reserve.reservation.interfaces.dto;

import io.hhplus.reserve.reservation.domain.ReserveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReserveResponse {

    @Getter
    @Builder
    @Schema(name = "ReserveResponse.Reserve", description = "선점 결과 객체")
    public static class Reserve {

        @Schema(description = "회원 ID", example = "1")
        private Long userId;

        @Schema(description = "좌석 ID 목록", example = "[1, 2, 3]")
        private List<Long> seatIdList;

        public static Reserve of(ReserveInfo.Reserve info) {
            return Reserve.builder()
                    .userId(info.getUserId())
                    .seatIdList(info.getSeatIdList())
                    .build();
        }
    }

}
