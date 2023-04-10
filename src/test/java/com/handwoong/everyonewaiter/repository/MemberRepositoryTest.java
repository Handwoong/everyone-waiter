package com.handwoong.everyonewaiter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.member.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberRequestDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRequestDto("test@test.com",
                "password1", "handwoong", "01012345678");
    }

    @Test
    @DisplayName("이메일 중복 검사")
    void existEmail() throws Exception {
        // given
        String email = "test@test.com";
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        boolean isExistEmail = memberRepository.existsByEmail(email);

        // then
        assertThat(isExistEmail).isTrue();
    }

    @Test
    @DisplayName("이메일 중복X")
    void notExistEmail() throws Exception {
        // given
        String email = "test@test.com";

        // when
        boolean isExistEmail = memberRepository.existsByEmail(email);

        // then
        assertThat(isExistEmail).isFalse();
    }

    @Test
    @DisplayName("휴대폰 번호 중복 검사")
    void existPhoneNumber() throws Exception {
        // given
        String phoneNumber = "01012345678";
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        boolean isExistPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(isExistPhoneNumber).isTrue();
    }

    @Test
    @DisplayName("휴대폰 번호 중복X")
    void notExistPhoneNumber() throws Exception {
        // given
        String phoneNumber = "01012345678";

        // when
        boolean isExistPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        // then
        assertThat(isExistPhoneNumber).isFalse();
    }
}
