package com.handwoong.everyonewaiter.dto.store;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.embedded.StoreBreakTime;
import com.handwoong.everyonewaiter.embedded.StoreBusinessTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class StoreResDto {

    private Long id;

    private String name;

    private String telephoneNumber;

    private StoreBusinessTime businessTime;

    private StoreBreakTime breakTime;

    public static StoreResDto from(Store store) {
        return StoreResDto.builder()
                .id(store.getId())
                .name(store.getName())
                .telephoneNumber(store.getTelephoneNumber())
                .businessTime(store.getBusinessTime())
                .breakTime(store.getBreakTime())
                .build();
    }
}
