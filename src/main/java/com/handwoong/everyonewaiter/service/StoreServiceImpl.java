package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.enums.ErrorCode.MEMBER_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.STORE_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.TELEPHONE_NUMBER_EXISTS;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.StoreDto;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import java.util.List;
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
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long register(String username, StoreDto.RequestDto storeDto) {
        Member findMember = findMemberByUsername(username);
        existsTelephone(username, storeDto.getTelephoneNumber());

        Store store = Store.createStore(storeDto, findMember);
        storeRepository.save(store);

        return store.getId();
    }

    @Override
    @Transactional
    public void update(String username, Long storeId, StoreDto.RequestDto storeDto) {
        existsTelephone(username, storeDto.getTelephoneNumber());
        Store store = findMemberStore(username, storeId);
        store.updateStore(storeDto);
    }

    @Override
    @Transactional
    public void delete(String username, Long storeId) {
        Store store = findMemberStore(username, storeId);
        storeRepository.delete(store);
    }

    @Override
    public List<StoreDto.ResponseDto> findStoreList(String username) {
        Member findMember = findMemberByUsername(username);
        List<Store> storeList = storeRepository.findAllByMemberId(findMember.getId());

        return storeList.stream()
                .map(StoreDto.ResponseDto::from)
                .toList();
    }

    @Override
    public StoreDto.ResponseDto findStore(String username, Long storeId) {
        Member findMember = findMemberByUsername(username);
        List<Store> storeList = storeRepository.findMemberStoreList(
                findMember.getUsername(), storeId, PageRequest.of(0, 1));

        if (storeList.isEmpty()) {
            throw new CustomException(STORE_NOT_FOUND);
        }

        return StoreDto.ResponseDto.from(storeList.get(0));
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            log.error("[{}] 존재하지 않는 회원 조회 = 로그인 아이디 : '{}'", transactionName, username);
            return new CustomException(MEMBER_NOT_FOUND);
        });
    }

    private Store findMemberStore(String username, Long storeId) {
        return storeRepository.findMemberStore(username, storeId).orElseThrow(() -> {
            String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            log.error("[{}] 존재하지 않는 매장 수정 요청 = 로그인 아이디 : '{}', 매장 ID : '{}'",
                    transactionName, username, storeId);
            return new CustomException(STORE_NOT_FOUND);
        });
    }

    private void existsTelephone(String username, String telephoneNumber) {
        boolean existTelNumber = storeRepository.existTelNumber(username, telephoneNumber);
        if (existTelNumber) {
            throw new CustomException(TELEPHONE_NUMBER_EXISTS);
        }
    }
}
