package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingReqDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingResDto;
import java.util.List;
import java.util.UUID;

public interface WaitingService {

    WaitingCountResDto count(String username, Long storeId);

    WaitingResDto findWaiting(UUID waitingId);

    List<WaitingResDto> findDefaultWaitingList(String username, Long storeId);

    UUID register(String username, Long storeId, WaitingReqDto waitingDto);

    void enterWaiting(String username, Long storeId, UUID waitingId);

    void cancelWaiting(UUID waitingId);
}
