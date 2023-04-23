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

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void beforeEach() {
        MemberDto memberDto = new MemberDto("handwoong", "password1",
                "01012345678");
        member = memberRepository.save(Member.createMember(memberDto));
    }

    @Test
    @DisplayName("회원 아이디로 매장 목록 조회")
    void findAllByMemberId() throws Exception {
        // given
        StoreDto storeDto1 = new StoreDto("나루", "055-123-4567");
        StoreDto storeDto2 = new StoreDto("가배", "031-123-4567");
        Store storeA = Store.createStore(storeDto1, member);
        Store storeB = Store.createStore(storeDto2, member);
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
}
