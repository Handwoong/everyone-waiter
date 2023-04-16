package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.store.StoreRequestDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import java.util.List;

public interface StoreService {

    Long register(String userEmail, StoreRequestDto storeDto);

    List<StoreResponseDto> findStoreList(String userEmail);
}
