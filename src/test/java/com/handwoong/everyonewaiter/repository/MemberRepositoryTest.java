package com.handwoong.everyonewaiter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("로그인 아이디 중복 검사")
    void existUsername() throws Exception {
        // given
        String username = "handwoong";
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        boolean isExistsUsername = memberRepository.existsByUsername(username);

        // then
        assertThat(isExistsUsername).isTrue();
    }

    @Test
    @DisplayName("로그인 아이디 중복X")
    void notExistUsername() throws Exception {
        // given
        String username = "handwoong";

        // when
        boolean isExistsUsername = memberRepository.existsByUsername(username);

        // then
        assertThat(isExistsUsername).isFalse();
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

    @Test
    @DisplayName("로그인 아이디로 회원 조회")
    void findByUsername() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findByUsername("handwoong").orElseThrow();

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
    }
}
