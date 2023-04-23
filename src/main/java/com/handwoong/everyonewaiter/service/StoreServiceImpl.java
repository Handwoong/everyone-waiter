package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.store.StoreDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Store store = Store.createStore(storeDto, findMember);
        storeRepository.save(store);

        log.info(
                "매장 생성 = 아이디 : '{}', 이름 : '{}', 매장 전화번호 : '{}', 회원 아이디 : '{}', 회원 로그인 아이디 : '{}'",
                store.getId(), store.getName(), store.getTelephoneNumber(),
                findMember.getId(), username);
        return store.getId();
    }

    @Override
    public List<StoreResponseDto> findStoreList(String username) {
        Member findMember = findMemberByUsername(username);

        List<Store> storeList = storeRepository.findAllByMemberId(findMember.getId());

        log.info("매장 전체 목록 조회 = 회원 로그인 아이디 : '{}', 매장 : '{}'개",
                username, storeList.size());
        return storeList.stream()
                .map(StoreResponseDto::from)
                .toList();
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            log.error("[{}] 존재하지 않는 회원 조회 = 로그인 아이디 : {}",
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    username);
            return new ResourceNotFoundException("존재하지 않는 회원 입니다.");
        });
    }
}
