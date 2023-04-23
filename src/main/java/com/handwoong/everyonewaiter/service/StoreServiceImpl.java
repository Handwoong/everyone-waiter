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
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> {
            log.error("존재하지 않는 회원 로그인 아이디로 매장 생성 요청 = 찾으려는 로그인 아이디 : '{}'", username);
            return new ResourceNotFoundException("존재하지 않는 회원 입니다.");
        });

        Store store = Store.createStore(storeDto, findMember);
        storeRepository.save(store);
        log.info("매장 생성 = 아이디 : '{}', 이름 : '{}', 회원 아이디 : '{}', 회원 로그인 아이디 : '{}'",
                store.getId(), store.getName(), findMember.getId(),
                findMember.getUsername());
        return store.getId();
    }

    @Override
    public List<StoreResponseDto> findStoreList(String username) {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> {
            log.error("존재하지 않는 회원 로그인 아이디로 매장 목록 조회 요청 = 찾으려는 로그인 아이디 : '{}'", username);
            return new ResourceNotFoundException("존재하지 않는 회원 입니다.");
        });

        List<Store> storeList = storeRepository.findAllByMemberId(findMember.getId());
        log.info("매장 전체 목록 조회 = 회원 이메일 : '{}', 매장 : '{}'개", username, storeList.size());
        return storeList.stream()
                .map(StoreResponseDto::from)
                .toList();
    }
}
