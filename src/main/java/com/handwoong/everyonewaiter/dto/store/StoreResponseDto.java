package com.handwoong.everyonewaiter.dto.store;

import com.handwoong.everyonewaiter.domain.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class StoreResponseDto {

    private Long id;

    private String name;

    public static StoreResponseDto from(Store store) {
        Assert.notNull(store, "(null) 매장 엔티티를 responseDto 변환에 실패하였습니다.");
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .build();
    }
}
