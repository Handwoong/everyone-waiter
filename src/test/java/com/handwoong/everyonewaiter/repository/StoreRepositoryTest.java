package com.handwoong.everyonewaiter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.store.StoreDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    private StoreDto storeDto;

    @BeforeEach
    void beforeEach() {
        MemberDto memberDto = new MemberDto("handwoong", "password1",
                "01012345678");
        member = memberRepository.save(Member.createMember(memberDto));
        storeDto = new StoreDto("나루", "055-123-4567");
    }

    @Test
    @DisplayName("회원 아이디로 매장 목록 조회")
    void findAllByMemberId() throws Exception {
        // given
        StoreDto storeDtoA = new StoreDto("가배", "031-123-4567");
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
        assertThat(findStoreA.getTelephoneNumber())
                .isEqualTo(storeA.getTelephoneNumber());

        assertThat(findStoreB.getName()).isEqualTo("가배");
        assertThat(findStoreB.getTelephoneNumber())
                .isEqualTo(storeB.getTelephoneNumber());
    }

    @Test
    @DisplayName("회원 로그인 아이디로 매장 목록 조회 - List")
    void findMemberStoreList() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        List<Store> storeList = storeRepository
                .findMemberStoreList(member.getUsername(), store.getId(),
                        PageRequest.of(0, 1));
        Store findStore = storeList.get(0);

        // then
        assertThat(storeList.size()).isEqualTo(1);
        assertThat(findStore.getName()).isEqualTo(storeDto.getName());
        assertThat(findStore.getTelephoneNumber())
                .isEqualTo(storeDto.getTelephoneNumber());
    }

    @Test
    @DisplayName("회원 로그인 아이디로 매장 목록 조회 - Page")
    void findMemberStorePage() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeRepository.save(store);

        // when
        Page<Store> page = storeRepository
                .findMemberStorePage(member.getUsername(), store.getId(),
                        PageRequest.of(0, 1));
        List<Store> storeList = page.getContent();
        Store findStore = storeList.get(0);

        // then
        assertThat(storeList.size()).isEqualTo(1);
        assertThat(findStore.getName()).isEqualTo(storeDto.getName());
        assertThat(findStore.getTelephoneNumber())
                .isEqualTo(storeDto.getTelephoneNumber());

        assertThat(page.getSize()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
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
}
