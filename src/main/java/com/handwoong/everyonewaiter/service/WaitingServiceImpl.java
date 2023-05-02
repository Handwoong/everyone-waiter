package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.enums.ErrorCode.STORE_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.WAITING_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.WaitingStatus.CANCEL;
import static com.handwoong.everyonewaiter.enums.WaitingStatus.DEFAULT;
import static com.handwoong.everyonewaiter.enums.WaitingStatus.ENTER;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.dto.WaitingDto;
import com.handwoong.everyonewaiter.enums.WaitingStatus;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import com.handwoong.everyonewaiter.repository.WaitingRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingServiceImpl implements WaitingService {

    private final WaitingRepository waitingRepository;

    private final StoreRepository storeRepository;

    @Override
    public WaitingDto.CountResponseDto count(String username, Long storeId) {
        isExistsMemberStore(username, storeId);
        Long waitingCount = waitingRepository.countByStoreId(storeId);

        return WaitingDto.CountResponseDto.from(waitingCount);
    }

    @Override
    public WaitingDto.ResponseDto findWaiting(UUID waitingId) {
        Waiting waiting = findById(waitingId);

        return WaitingDto.ResponseDto.from(waiting);
    }

    @Override
    public List<WaitingDto.ResponseDto> findDefaultWaitingList(String username, Long storeId) {
        isExistsMemberStore(username, storeId);
        List<Waiting> waitingList = waitingRepository.findStatusWaitingList(storeId, DEFAULT);

        return waitingList.stream()
                .map(WaitingDto.ResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public UUID register(String username, Long storeId, WaitingDto.RequestDto waitingDto) {
        isExistsMemberStore(username, storeId);
        PageRequest page = PageRequest.of(0, 1);

        Store store = storeRepository.findStoreById(storeId);
        Waiting lastWaiting = checkEmptyList(waitingRepository.findLastWaiting(storeId, page));
        Waiting defaultLastWaiting = checkEmptyList(
                waitingRepository.findStatusLastWaiting(storeId, DEFAULT, page));

        Waiting waiting = Waiting.createWaiting(waitingDto, store, lastWaiting, defaultLastWaiting);
        waitingRepository.save(waiting);

        return waiting.getId();
    }

    @Override
    @Transactional
    public void enterWaiting(String username, Long storeId, UUID waitingId) {
        isExistsMemberStore(username, storeId);
        findWaitingAndChangeStatus(waitingId, ENTER);
    }

    @Override
    @Transactional
    public void cancelWaiting(UUID waitingId) {
        findWaitingAndChangeStatus(waitingId, CANCEL);
    }

    private void isExistsMemberStore(String username, Long storeId) {
        storeRepository.findMemberStore(username, storeId)
                .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
    }

    private Waiting checkEmptyList(List<Waiting> waitingList) {
        if (waitingList.isEmpty()) {
            return null;
        }
        return waitingList.get(0);
    }

    private void findWaitingAndChangeStatus(UUID waitingId, WaitingStatus status) {
        Waiting waiting = findById(waitingId);
        Long storeId = waiting.getStore().getId();
        waitingRepository.decreaseWaitingTurn(storeId, waiting.getWaitingTurn());
        waiting.changeEnterOrCancelStatus(status);
    }

    private Waiting findById(UUID waitingId) {
        return waitingRepository.findById(waitingId)
                .orElseThrow(() -> new CustomException(WAITING_NOT_FOUND));
    }
}
