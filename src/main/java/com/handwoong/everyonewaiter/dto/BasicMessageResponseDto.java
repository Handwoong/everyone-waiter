package com.handwoong.everyonewaiter.dto;

import lombok.Data;

@Data
public class BasicMessageResponseDto {

    private String message;

    public BasicMessageResponseDto(String message) {
        this.message = message;
    }
}
