package com.handwoong.everyonewaiter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.StoreDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    private StoreDto.RequestDto storeDto;

    @BeforeEach
    void beforeEach() {
        MemberDto.RequestDto memberDto =
                new MemberDto.RequestDto("handwoong", "password1", "01012345678");
        member = memberRepository.save(Member.createMember(memberDto));
        storeDto = new StoreDto.RequestDto("나루", "055-123-4567");
    }

    @Test
    @DisplayName("매장 저장")
    void saveStore() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);

        // when
        Store saveStore = storeRepository.save(store);

        // then
        assertThat(saveStore.getId()).isEqualTo(store.getId());
    }

    @Test
    @DisplayName("회원 아이디로 매장 목록 조회")
    void findAllByMemberId() throws Exception {
        // given
        StoreDto.RequestDto storeDtoA = new StoreDto.RequestDto("가배", "031-123-4567");
        Store storeA = Store.createStore(storeDto, member);
        Store storeB = Store.createStore(storeDtoA, member);
        storeRepository.save(storeA);
        storeRepository.save(storeB);

        // when
        List<Store> storeList = storeRepository.findAllByMemberId(member.getId());
        Store findStoreA = storeList.get(0);
        Store findStoreB = storeList.get(1);

        // then
        assertThat(storeList.size()).isEqualTo(2);

        assertThat(findStoreA.getName()).isEqualTo("나루");
        assertThat(findStoreA.getTelephoneNumber()).isEqualTo(storeA.getTelephoneNumber());

        assertThat(findStoreB.getName()).isEqualTo("가배");
        assertThat(findStoreB.getTelephoneNumber()).isEqualTo(storeB.getTelephoneNumber());
    }

    @Test
    @DisplayName("매장 아이디로 단건 조회")
    void findStoreById() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        Store findStore = storeRepository.findStoreById(store.getId());

        // then
        assertThat(findStore.getId()).isEqualTo(store.getId());
        assertThat(findStore.getName()).isEqualTo(store.getName());
        assertThat(findStore.getTelephoneNumber()).isEqualTo(store.getTelephoneNumber());
    }

    @Test
    @DisplayName("매장 전화번호 중복")
    void existsTelephone() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        boolean existTelNumber =
                storeRepository.existTelNumber("username", "055-123-4567");

        // then
        assertThat(existTelNumber).isTrue();
    }

    @Test
    @DisplayName("매장 전화번호 중복X")
    void notExistsTelephone() throws Exception {
        boolean existTelNumber =
                storeRepository.existTelNumber(member.getUsername(), "055-123-4567");

        // then
        assertThat(existTelNumber).isFalse();
    }

    @Test
    @DisplayName("회원 로그인 아이디와 매장 아이디가 일치하는 매장 조회")
    void findMemberStore() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        Store findStore =
                storeRepository.findMemberStore(member.getUsername(), store.getId()).orElseThrow();

        // then
        assertThat(findStore.getId()).isEqualTo(store.getId());
    }

    @Test
    @DisplayName("매장 삭제")
    void deleteStore() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);

        // when
        storeRepository.delete(store);
        List<Store> storeList = storeRepository.findAll();

        // then
        assertThat(storeList.size()).isEqualTo(0);
    }
}
