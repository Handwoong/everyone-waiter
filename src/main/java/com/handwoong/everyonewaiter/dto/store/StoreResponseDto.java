package com.handwoong.everyonewaiter.dto.store;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.domain.StoreBreakTime;
import com.handwoong.everyonewaiter.domain.StoreBusinessTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class StoreResponseDto {

    private Long id;

    private String name;

    private String telephoneNumber;

    private StoreBusinessTime businessTime;

    private StoreBreakTime breakTime;

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .telephoneNumber(store.getTelephoneNumber())
                .businessTime(store.getBusinessTime())
                .breakTime(store.getBreakTime())
                .build();
    }
}
