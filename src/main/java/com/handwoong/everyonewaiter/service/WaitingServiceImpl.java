package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.domain.WaitingStatus.CANCEL;
import static com.handwoong.everyonewaiter.domain.WaitingStatus.DEFAULT;
import static com.handwoong.everyonewaiter.domain.WaitingStatus.ENTER;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.domain.WaitingStatus;
import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponseDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import com.handwoong.everyonewaiter.repository.WaitingRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingServiceImpl implements WaitingService {

    private final WaitingRepository waitingRepository;

    private final StoreRepository storeRepository;

    @Override
    public WaitingCountResponseDto count(String username, Long storeId) {
        isExistsMemberStore(username, storeId);

        Long waitingCount = waitingRepository.countByStoreId(storeId);

        log.info("매장의 현재 웨이팅 수 = 매장 아이디 : '{}', 웨이팅 수 : '{}'", storeId, waitingCount);
        return WaitingCountResponseDto.from(waitingCount);
    }

    @Override
    public WaitingResponseDto findWaiting(UUID waitingId) {
        Waiting waiting = findById(waitingId);

        log.info(
                "웨이팅 단건 조회 요청 = 웨이팅 아이디 : '{}', 대기 번호 : '{}', 대기 순번 : '{}', 상태 : '{}', 휴대폰 번호 : '{}', 메시지 전송 여부 : '{}', 등록일 : '{}'",
                waiting.getId(), waiting.getWaitingNumber(), waiting.getWaitingTurn(),
                waiting.getStatus(), waiting.getPhoneNumber(),
                waiting.isSendEnterMessage(), waiting.getCreatedAt());
        return WaitingResponseDto.from(waiting);
    }

    @Override
    public List<WaitingResponseDto> findDefaultWaitingList(String username,
            Long storeId) {
        isExistsMemberStore(username, storeId);

        List<Waiting> waitingList = waitingRepository
                .findStatusWaitingList(storeId, DEFAULT);

        log.info("웨이팅 목록 조회 요청 = 로그인 아이디 : '{}', 매장 아이디 : '{}', 웨이팅 '{}'개",
                username, storeId, waitingList.size());
        return waitingList.stream()
                .map(WaitingResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public UUID register(String username, Long storeId, WaitingDto waitingDto) {
        isExistsMemberStore(username, storeId);
        PageRequest page = PageRequest.of(0, 1);

        Store store = storeRepository.findStoreById(storeId);
        Waiting lastWaiting = checkEmptyList(
                waitingRepository.findLastWaiting(storeId, page));
        Waiting defaultLastWaiting = checkEmptyList(
                waitingRepository.findStatusLastWaiting(storeId, DEFAULT, page));

        Waiting waiting = Waiting
                .createWaiting(waitingDto, store, lastWaiting, defaultLastWaiting);
        waitingRepository.save(waiting);

        log.info(
                "웨이팅 등록 =  로그인 아이디 : '{}', 매장 아이디 : '{}', 웨이팅 아이디 : '{}', 대기 번호 : '{}', 대기 순번 : '{}', 상태 : '{}', 휴대폰 번호 : '{}', 등록일 : '{}'",
                username, store.getId(), waiting.getId(), waiting.getWaitingNumber(),
                waiting.getWaitingTurn(), waiting.getStatus(), waiting.getPhoneNumber(),
                waiting.getCreatedAt());
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
        List<Store> list = storeRepository.findMemberStoreList(username, storeId,
                PageRequest.of(0, 1));

        if (list.isEmpty()) {
            log.error("[{}] 회원의 존재하지 않는 매장 웨이팅 조회 요청 = 로그인 아이디 : '{}', 매장 아이디 : '{}'",
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    username, storeId);
            throw new ResourceExistsException("매장이 존재하지 않습니다.");
        }
    }

    private Waiting checkEmptyList(List<Waiting> waitingList) {
        if (waitingList.isEmpty()) {
            return null;
        }
        return waitingList.get(0);
    }

    private void findWaitingAndChangeStatus(UUID waitingId, WaitingStatus status) {
        Waiting waiting = findById(waitingId);
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        if (waiting.getStatus() != DEFAULT) {
            log.error("[{}] 웨이팅 현재 상태 대기가 아닌데 상태 변경 요청 = 웨이팅 아이디 : '{}'",
                    transactionName, waiting.getId());
            return;
        }

        waiting.changeEnterOrCancelStatus(status);
        Long storeId = waiting.getStore().getId();
        int decreaseCount = waitingRepository
                .decreaseWaitingTurn(storeId, waiting.getWaitingTurn());

        log.info(
                "[{}] 웨이팅 상태 변경 = 매장 아이디 : '{}', 웨이팅 아이디 : '{}', 대기 번호 : '{}', 변경된 상태 : '{}', 등록일 : '{}', 웨이팅 순번 감소 개수 : '{}'",
                transactionName,
                storeId, waiting.getId(), waiting.getWaitingNumber(), waiting.getStatus(),
                waiting.getCreatedAt(), decreaseCount);
    }

    private Waiting findById(UUID waitingId) {
        return waitingRepository.findById(waitingId).orElseThrow(() -> {
            log.error("[{}] 존재하지 않는 웨이팅 단건 조회 요청 = 웨이팅 아이디 : '{}'",
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    waitingId);
            return new ResourceNotFoundException("존재하지 않는 웨이팅 입니다.");
        });
    }
}
