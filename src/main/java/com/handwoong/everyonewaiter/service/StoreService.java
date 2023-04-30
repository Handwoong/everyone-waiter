package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.StoreDto;
import java.util.List;

public interface StoreService {

    Long register(String username, StoreDto.RequestDto storeDto);

    void update(String username, Long storeId, StoreDto.RequestDto storeDto);

    void delete(String username, Long storeId);

    StoreDto.ResponseDto findStore(String username, Long storeId);

    List<StoreDto.ResponseDto> findStoreList(String username);
}
