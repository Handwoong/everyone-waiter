package com.handwoong.everyonewaiter.dto;

import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.enums.WaitingStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WaitingDto {

    @Data
    @NoArgsConstructor
    public static class RequestDto {

        @Min(value = 0, message = "{error.message.min}")
        @Max(value = 20, message = "{error.message.max}")
        private int adult;

        @Min(value = 0, message = "{error.message.min}")
        @Max(value = 20, message = "{error.message.max}")
        private int children;

        @NotNull(message = "{error.message.null}")
        @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
                message = "{error.message.phoneNumber}")
        private String phoneNumber;

        public RequestDto(int adult, int children, String phoneNumber) {
            this.adult = adult;
            this.children = children;
            this.phoneNumber = phoneNumber;
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ResponseDto {

        private UUID id;

        private int waitingNumber;

        private int waitingTurn;

        private int adult;

        private int children;

        private String phoneNumber;

        private WaitingStatus status;

        private boolean isSendEnterMessage;

        private boolean isSendReadyMessage;

        private LocalDateTime createdAt;

        public static ResponseDto from(Waiting waiting) {
            return ResponseDto.builder()
                    .id(waiting.getId())
                    .waitingNumber(waiting.getWaitingNumber())
                    .waitingTurn(waiting.getWaitingTurn())
                    .adult(waiting.getAdult())
                    .children(waiting.getChildren())
                    .phoneNumber(waiting.getPhoneNumber())
                    .status(waiting.getStatus())
                    .isSendEnterMessage(waiting.isSendEnterMessage())
                    .isSendReadyMessage(waiting.isSendReadyMessage())
                    .createdAt(waiting.getCreatedAt())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CountResponseDto {

        private Long count;

        public static CountResponseDto from(Long count) {
            return CountResponseDto.builder()
                    .count(count)
                    .build();
        }
    }
}
