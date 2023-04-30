package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.store.StoreReqDto;
import com.handwoong.everyonewaiter.dto.store.StoreResDto;
import java.util.List;

public interface StoreService {

    Long register(String username, StoreReqDto storeDto);

    void update(String username, Long storeId, StoreReqDto storeDto);

    void delete(String username, Long storeId);

    StoreResDto findStore(String username, Long storeId);

    List<StoreResDto> findStoreList(String username);
}
