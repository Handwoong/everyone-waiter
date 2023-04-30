package com.handwoong.everyonewaiter.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.uuid.Generators;
import com.handwoong.everyonewaiter.dto.WaitingDto;
import com.handwoong.everyonewaiter.enums.WaitingStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Waiting extends BaseEntity {

    @Id
    @Column(name = "waiting_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private int waitingNumber;

    private int waitingTurn;

    private int adult;

    private int children;

    @NotNull
    @Column(length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private WaitingStatus status;

    private boolean isSendEnterMessage;

    private boolean isSendReadyMessage;

    @Builder(access = PRIVATE)
    private Waiting(Store store, int waitingNumber, int waitingTurn, int adult,
            int children, String phoneNumber, WaitingStatus status,
            boolean isSendEnterMessage, boolean isSendReadyMessage) {
        this.store = store;
        this.waitingNumber = waitingNumber;
        this.waitingTurn = waitingTurn;
        this.adult = adult;
        this.children = children;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.isSendEnterMessage = isSendEnterMessage;
        this.isSendReadyMessage = isSendReadyMessage;
    }

    @PrePersist
    private void createWaitingUuid() {
        // UUID V1
        // Sequential UUID
        UUID uuid = Generators.timeBasedGenerator().generate();
        String[] uuidArr = uuid.toString().split("-");
        String uuidStr = uuidArr[2] + uuidArr[1] + uuidArr[0] + uuidArr[3] + uuidArr[4];
        StringBuilder sb = new StringBuilder(uuidStr);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        this.id = UUID.fromString(sb.toString());
    }

    /**
     * @param lastWaiting        대기, 입장, 취소 상태에 상관없이 마지막 웨이팅
     * @param defaultLastWaiting 대기 상태의 마지막 웨이팅
     */
    public static Waiting createWaiting(WaitingDto.RequestDto waitingDto, Store store,
            @Nullable Waiting lastWaiting, @Nullable Waiting defaultLastWaiting) {
        int waitingNumber = 1;
        int waitingTurn = 0;

        if (lastWaiting != null) {
            waitingNumber += lastWaiting.getWaitingNumber();
        }

        if (defaultLastWaiting != null) {
            waitingTurn += defaultLastWaiting.getWaitingTurn() + 1;
        }

        return Waiting.builder()
                .store(store)
                .waitingNumber(waitingNumber)
                .waitingTurn(waitingTurn)
                .adult(waitingDto.getAdult())
                .children(waitingDto.getChildren())
                .phoneNumber(waitingDto.getPhoneNumber())
                .status(WaitingStatus.DEFAULT)
                .isSendEnterMessage(false)
                .isSendReadyMessage(false)
                .build();
    }

    public void changeEnterOrCancelStatus(WaitingStatus status) {
        if (status == WaitingStatus.DEFAULT) {
            return;
        }
        this.status = status;
        this.waitingTurn = -1;
    }

    public void changeSendEnterStatus() {
        this.isSendEnterMessage = true;
    }

    public void changeSendReadyStatus() {
        this.isSendReadyMessage = true;
    }
}
