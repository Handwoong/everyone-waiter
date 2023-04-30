package com.handwoong.everyonewaiter.dto;

import lombok.Data;

@Data
public class OnlyMsgResDto {

    private String message;

    public OnlyMsgResDto(String message) {
        this.message = message;
    }
}
