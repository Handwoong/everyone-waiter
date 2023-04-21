package com.handwoong.everyonewaiter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.store.StoreRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoreTest {

    private StoreRequestDto storeRequestDto;

    private Member member;

    @BeforeEach
    void beforeEach() {
        MemberDto memberDto = new MemberDto("handwoong", "password",
                "01012345678");
        member = Member.createMember(memberDto);
        storeRequestDto = new StoreRequestDto("나루");
    }

    @Test
    @DisplayName("매장 엔티티 생성")
    void createStore() throws Exception {
        Store store = Store.createStore(storeRequestDto, member);
        assertThat(store.getName()).isEqualTo("나루");
        assertThat(store.getMember().getUsername()).isEqualTo("handwoong");
    }
}
