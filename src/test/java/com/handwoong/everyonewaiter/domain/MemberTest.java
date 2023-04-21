package com.handwoong.everyonewaiter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.member.MemberRegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {

    private MemberRegisterDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRegisterDto("handwoong", "password", "01012345678");
    }

    @Test
    @DisplayName("회원 엔티티 생성")
    void createMember() throws Exception {
        Member member = Member.createMember(memberDto);
        assertThat(member.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(member.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(member.getBalance()).isEqualTo(0);
        assertThat(member.getRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("회원 비밀번호 암호화")
    void encodePassword() throws Exception {
        // given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Member member = Member.createMember(memberDto);

        // when
        member.encodePassword(passwordEncoder.encode(member.getPassword()));

        // then
        assertThat(member.getPassword()).isNotEqualTo("password");
    }
}
