package com.handwoong.everyonewaiter.dto.waiting;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class WaitingCountResponseDto {

    private Long count;

    public static WaitingCountResponseDto from(Long count) {
        return WaitingCountResponseDto.builder()
                .count(count)
                .build();
    }
}
