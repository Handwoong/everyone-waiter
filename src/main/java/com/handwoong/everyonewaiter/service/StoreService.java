package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.store.StoreRequestDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import java.util.List;

public interface StoreService {

    Long register(StoreRequestDto storeDto, String userEmail);

    List<StoreResponseDto> findStoreList(String userEmail);
}
