package com.handwoong.everyonewaiter.dto.waiting;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class WaitingCountResDto {

    private Long count;

    public static WaitingCountResDto from(Long count) {
        return WaitingCountResDto.builder()
                .count(count)
                .build();
    }
}
