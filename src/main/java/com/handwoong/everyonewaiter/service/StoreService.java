package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.store.StoreReqDto;
import com.handwoong.everyonewaiter.dto.store.StoreResDto;
import java.util.List;

public interface StoreService {

    Long register(String username, StoreReqDto storeDto);

    StoreResDto findStore(String username, Long storeId);

    List<StoreResDto> findStoreList(String username);
}
