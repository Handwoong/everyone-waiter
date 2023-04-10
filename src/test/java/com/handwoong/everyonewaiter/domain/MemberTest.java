package com.handwoong.everyonewaiter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.member.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private MemberRequestDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRequestDto("test@test.com",
                "password", "handwoong", "01012345678");
    }

    @Test
    @DisplayName("회원 엔티티 생성")
    void createMember() throws Exception {
        Member member = Member.createMember(memberDto);
        assertThat(member.getEmail()).isEqualTo(memberDto.getEmail());
        assertThat(member.getName()).isEqualTo(memberDto.getName());
        assertThat(member.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(member.getBalance()).isEqualTo(0);
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
    }
}
