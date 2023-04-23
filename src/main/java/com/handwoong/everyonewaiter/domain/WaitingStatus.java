package com.handwoong.everyonewaiter.domain;

import lombok.Getter;

@Getter
public enum WaitingStatus {

    DEFAULT("대기"), ENTER("입장"), CANCEL("취소");

    private final String description;

    WaitingStatus(String description) {
        this.description = description;
    }
}
