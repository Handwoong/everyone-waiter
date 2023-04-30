package com.handwoong.everyonewaiter.dto.waiting;

import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.enums.WaitingStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class WaitingResDto {

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

    public static WaitingResDto from(Waiting waiting) {
        return WaitingResDto.builder()
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
