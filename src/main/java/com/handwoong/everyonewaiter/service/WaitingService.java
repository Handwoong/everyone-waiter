package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.WaitingDto;
import java.util.List;
import java.util.UUID;

public interface WaitingService {

    WaitingDto.CountResponseDto count(String username, Long storeId);

    WaitingDto.ResponseDto findWaiting(UUID waitingId);

    List<WaitingDto.ResponseDto> findDefaultWaitingList(String username, Long storeId);

    UUID register(String username, Long storeId, WaitingDto.RequestDto waitingDto);

    void enterWaiting(String username, Long storeId, UUID waitingId);

    void cancelWaiting(UUID waitingId);
}
