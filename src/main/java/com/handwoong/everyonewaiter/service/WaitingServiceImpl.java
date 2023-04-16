package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.domain.WaitingStatus;
import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponseDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingRequestDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import com.handwoong.everyonewaiter.repository.WaitingRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public WaitingCountResponseDto count(Long storeId) {
        isExistsStore(storeId);
        Long waitingCount = waitingRepository.countByStoreId(storeId);
        log.info("매장의 현재 웨이팅 수 = 매장 아이디 : '{}', 웨이팅 수 : '{}'", storeId, waitingCount);
        return WaitingCountResponseDto.from(waitingCount);
    }

    @Override
    public WaitingResponseDto findWaiting(UUID waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(() -> {
            log.error("존재하지 않는 웨이팅 조회 요청 = 웨이팅 아이디 : '{}'", waitingId);
            return new ResourceNotFoundException("존재하지 않는 웨이팅 입니다.");
        });

        log.info(
                "웨이팅 단건 조회 요청 = 아이디 : '{}', 대기 번호 : '{}', 대기 순번 : '{}', 상태 : '{}', 휴대폰 번호 : '{}', 메시지 전송 여부 : '{}', 등록일 : '{}'",
                waiting.getId(), waiting.getWaitingNumber(), waiting.getWaitingTurn(),
                waiting.getStatus(), waiting.getPhoneNumber(),
                waiting.isSendEnterMessage(), waiting.getCreatedAt());
        return WaitingResponseDto.from(waiting);
    }

    @Override
    public List<WaitingResponseDto> findDefaultWaitingList(Long storeId) {
        List<Waiting> waitingList = waitingRepository.findDefaultWaitingList(storeId,
                WaitingStatus.DEFAULT);
        log.info("웨이팅 목록 조회 요청 = 웨이팅 '{}'개", waitingList.size());
        return waitingList.stream()
                .map(WaitingResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public UUID register(Long storeId, WaitingRequestDto waitingDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> {
            log.error("존재하지 않는 매장 웨이팅 등록 요청 = 매장 아이디 : '{}', 휴대폰 번호 : '{}'", storeId,
                    waitingDto.getPhoneNumber());
            return new ResourceNotFoundException("매장이 존재하지 않습니다.");
        });

        Waiting lastWaiting = waitingRepository.findLastWaiting(storeId);
        List<Waiting> waitingList = waitingRepository.findDefaultWaitingList(storeId,
                WaitingStatus.DEFAULT);

        Waiting waiting = Waiting.createWaiting(waitingDto, store, lastWaiting,
                waitingList);
        waitingRepository.save(waiting);

        log.info(
                "웨이팅 등록 = 아이디 : '{}', 매장 아이디 : '{}', 대기 번호 : '{}', 대기 순번 : '{}', 상태 : '{}', 휴대폰 번호 : '{}', 등록일 : '{}'",
                waiting.getId(), store.getId(), waiting.getWaitingNumber(),
                waiting.getWaitingTurn(), waiting.getStatus(), waiting.getPhoneNumber(),
                waiting.getCreatedAt());
        return waiting.getId();
    }

    @Override
    @Transactional
    public void enterWaiting(Long storeId, UUID waitingId) {
        isExistsStore(storeId);
        findWaitingAndChangeStatus(storeId, waitingId, WaitingStatus.ENTER);
    }

    @Override
    @Transactional
    public void cancelWaiting(Long storeId, UUID waitingId) {
        isExistsStore(storeId);
        findWaitingAndChangeStatus(storeId, waitingId, WaitingStatus.CANCEL);
    }

    private void isExistsStore(Long storeId) {
        boolean isExistsStore = storeRepository.existsById(storeId);
        if (!isExistsStore) {
            log.error("존재하지 않는 매장 웨이팅 조회 요청 = 매장 아이디 : '{}'", storeId);
            throw new ResourceExistsException("매장이 존재하지 않습니다.");
        }
    }

    private void findWaitingAndChangeStatus(Long storeId, UUID waitingId,
            WaitingStatus status) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(() -> {
            log.error("존재하지 않는 웨이팅 단건 조회 요청 = 웨이팅 아이디 : '{}'", waitingId);
            return new ResourceNotFoundException("존재하지 않는 웨이팅 입니다.");
        });

        waiting.changeEnterOrCancelStatus(status);
        int decreaseCount = waitingRepository.decreaseWaitingTurn(storeId,
                waiting.getWaitingTurn());
        log.info(
                "웨이팅 상태 변경 = 웨이팅 아이디 : '{}', 대기 번호 : '{}', 변경된 상태 : '{}', 등록일 : '{}', 웨이팅 순번 감소 개수 : '{}'",
                waiting.getId(), waiting.getWaitingNumber(), waiting.getStatus(),
                waiting.getCreatedAt(), decreaseCount);
    }
}
