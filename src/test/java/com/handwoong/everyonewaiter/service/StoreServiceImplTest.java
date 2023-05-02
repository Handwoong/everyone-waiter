package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.StoreDto;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.FakeMemberRepository;
import com.handwoong.everyonewaiter.repository.FakeStoreRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoreServiceImplTest {

    private StoreServiceImpl storeService;

    private FakeStoreRepository storeRepository;

    private FakeMemberRepository memberRepository;

    private MemberDto.RequestDto memberDto;

    private StoreDto.RequestDto storeDto;

    @BeforeEach
    void beforeEach() {
        memberRepository = new FakeMemberRepository();
        storeRepository = new FakeStoreRepository();
        storeService = new StoreServiceImpl(storeRepository, memberRepository);

        memberDto = new MemberDto.RequestDto("handwoong", "password1", "01012345678");
        storeDto = new StoreDto.RequestDto("나루", "055-123-4567");
    }

    @AfterEach
    void afterEach() {
        memberRepository.clear();
        storeRepository.clear();
    }

    @Test
    @DisplayName("매장 등록")
    void register() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        storeService.register(member.getUsername(), storeDto);
        Store findStore = storeRepository.findById(1L).orElseThrow();

        // then
        assertThat(findStore.getName()).isEqualTo(storeDto.getName());
        assertThat(findStore.getTelephoneNumber()).isEqualTo(storeDto.getTelephoneNumber());
    }

    @Test
    @DisplayName("매장 등록 시 회원 로그인 아이디를 찾을 수 없음")
    void registerNotFoundUsername() throws Exception {
        assertThatThrownBy(() -> storeService.register("handwoong", storeDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("매장 등록 시 매장 전화번호가 중복이면 예외 발생")
    void registerExistsTelephone() throws Exception {
        // given
        Member member = memberRepository.save(Member.createMember(memberDto));
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // then
        assertThatThrownBy(() -> storeService.register("handwoong", storeDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("매장 삭제")
    void deleteStore() throws Exception {
        // given
        Member member = memberRepository.save(Member.createMember(memberDto));
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        storeService.delete("handwoong", 1L);
        long count = storeRepository.count();

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("매장 삭제 시 매장을 찾지 못하면 예외 발생")
    void deleteNotFoundStore() throws Exception {
        assertThatThrownBy(() -> storeService.delete("handwoong", 1L))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("회원의 매장 목록 조회")
    void findStoreList() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        Store storeA = Store.createStore(storeDto, member);
        storeRepository.save(storeA);

        storeDto.setName("가배");
        storeDto.setTelephoneNumber("031-123-4567");
        Store storeB = Store.createStore(storeDto, member);
        storeRepository.save(storeB);

        // when
        List<StoreDto.ResponseDto> storeList = storeService
                .findStoreList(member.getUsername());

        // then
        assertThat(storeList.size()).isEqualTo(2);
        assertThat(storeList.get(0).getName()).isEqualTo("나루");
        assertThat(storeList.get(1).getName()).isEqualTo("가배");
    }

    @Test
    @DisplayName("회원의 매장 목록 조회 시 회원 로그인 아이디를 찾을 수 없음")
    void findStoreListNotFoundUsername() throws Exception {
        assertThatThrownBy(() -> storeService.findStoreList("handwoong"))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }
}
