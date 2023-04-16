package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponseDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingRequestDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponseDto;
import java.util.List;
import java.util.UUID;

public interface WaitingService {

    WaitingCountResponseDto count(Long storeId);

    WaitingResponseDto findWaiting(UUID waitingId);

    List<WaitingResponseDto> findDefaultWaitingList(Long storeId);

    UUID register(Long storeId, WaitingRequestDto waitingDto);

    void enterWaiting(Long storeId, UUID waitingId);

    void cancelWaiting(Long storeId, UUID waitingId);
}
