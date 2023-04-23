package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.store.StoreDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import java.util.List;

public interface StoreService {

    Long register(String username, StoreDto storeDto);

    List<StoreResponseDto> findStoreList(String username);
}
