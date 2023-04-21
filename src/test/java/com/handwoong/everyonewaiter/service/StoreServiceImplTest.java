package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.member.MemberRegisterDto;
import com.handwoong.everyonewaiter.dto.store.StoreRequestDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StoreServiceImplTest {

    @Autowired
    StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void beforeEach() {
        MemberRegisterDto memberDto = new MemberRegisterDto("handwoong", "password1",
                "01012345678");
        member = memberRepository.save(Member.createMember(memberDto));
    }

    @Test
    @DisplayName("매장 등록")
    void register() throws Exception {
        // given
        StoreRequestDto storeDto = new StoreRequestDto("나루");
        Long storeId = storeService.register("handwoong", storeDto);

        // when
        Store store = storeRepository.findById(storeId).orElseThrow();

        // then
        assertThat(store.getId()).isEqualTo(storeId);
        assertThat(store.getName()).isEqualTo("나루");
        assertThat(store.getMember().getUsername()).isEqualTo("handwoong");
    }

    @Test
    @DisplayName("매장 등록 시 회원 로그인 아이디를 찾을 수 없음")
    void registerNotFoundUsername() throws Exception {
        StoreRequestDto storeDto = new StoreRequestDto("나루");
        assertThatThrownBy(() -> storeService.register("notfound", storeDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("회원의 매장 목록 조회")
    void findStoreList() throws Exception {
        // given
        StoreRequestDto storeDto1 = new StoreRequestDto("나루1");
        StoreRequestDto storeDto2 = new StoreRequestDto("나루2");
        Store storeA = Store.createStore(storeDto1, member);
        Store storeB = Store.createStore(storeDto2, member);
        storeRepository.save(storeA);
        storeRepository.save(storeB);

        // when
        List<StoreResponseDto> storeList = storeService.findStoreList("handwoong");

        // then
        assertThat(storeList.size()).isEqualTo(2);
        assertThat(storeList.get(0).getName()).isEqualTo("나루1");
        assertThat(storeList.get(0).getId()).isEqualTo(storeA.getId());
        assertThat(storeList.get(1).getName()).isEqualTo("나루2");
        assertThat(storeList.get(1).getId()).isEqualTo(storeB.getId());
    }

    @Test
    @DisplayName("회원의 매장 조회 시 회원 로그인 아이디를 찾을 수 없음")
    void findStoreListNotFoundUsername() throws Exception {
        assertThatThrownBy(() -> storeService.findStoreList("notfound@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
