package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponseDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponseDto;
import java.util.List;
import java.util.UUID;

public interface WaitingService {

    WaitingCountResponseDto count(String username, Long storeId);

    WaitingResponseDto findWaiting(UUID waitingId);

    List<WaitingResponseDto> findDefaultWaitingList(String username, Long storeId);

    UUID register(String username, Long storeId, WaitingDto waitingDto);

    void enterWaiting(String username, Long storeId, UUID waitingId);

    void cancelWaiting(UUID waitingId);
}
