package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.handwoong.everyonewaiter.exception.ErrorCode.STORE_NOT_FOUND;
import static com.handwoong.everyonewaiter.exception.ErrorCode.TELEPHONE_NUMBER_EXISTS;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.store.StoreDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
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
    public Long register(String username, StoreDto storeDto) {
        Member findMember = findMemberByUsername(username);
        boolean existTelNumber = storeRepository.existTelNumber(storeDto.getTelephoneNumber());
        if (existTelNumber) {
            throw new CustomException(TELEPHONE_NUMBER_EXISTS);
        }

        Store store = Store.createStore(storeDto, findMember);
        storeRepository.save(store);

        return store.getId();
    }

    @Override
    public List<StoreResponseDto> findStoreList(String username) {
        Member findMember = findMemberByUsername(username);
        List<Store> storeList = storeRepository.findAllByMemberId(findMember.getId());

        return storeList.stream()
                .map(StoreResponseDto::from)
                .toList();
    }


    @Override
    public StoreResponseDto findStore(String username, Long storeId) {
        Member findMember = findMemberByUsername(username);
        List<Store> storeList = storeRepository.findMemberStoreList(
                findMember.getUsername(), storeId, PageRequest.of(0, 1));

        if (storeList.isEmpty()) {
            throw new CustomException(STORE_NOT_FOUND);
        }

        return StoreResponseDto.from(storeList.get(0));
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            log.error("[{}] 존재하지 않는 회원 조회 = 로그인 아이디 : {}",
                    TransactionSynchronizationManager.getCurrentTransactionName(), username);
            return new CustomException(MEMBER_NOT_FOUND);
        });
    }
}
