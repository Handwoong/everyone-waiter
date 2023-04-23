package com.handwoong.everyonewaiter.dto;

import lombok.Data;

@Data
public class BasicResponseDto {

    private String message;

    public BasicResponseDto(String message) {
        this.message = message;
    }
}
