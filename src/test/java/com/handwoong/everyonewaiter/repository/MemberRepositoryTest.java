package com.handwoong.everyonewaiter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.enums.ErrorCode;
import com.handwoong.everyonewaiter.exception.CustomException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberDto.RequestDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberDto.RequestDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("회원 저장")
    void saveMember() throws Exception {
        // given
        Member member = Member.createMember(memberDto);

        // when
        Member saveMember = memberRepository.save(member);

        // then
        assertThat(saveMember.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원 전체 조회")
    void findAllMember() throws Exception {
        // given
        Member memberA = Member.createMember(memberDto);
        memberRepository.save(memberA);

        memberDto.setUsername("username");
        memberDto.setPhoneNumber("01011112222");
        Member memberB = Member.createMember(memberDto);
        memberRepository.save(memberB);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("로그인 아이디 중복 검사")
    void existUsername() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        String username = member.getUsername();
        String phoneNumber = member.getPhoneNumber();

        // when
        boolean isExistsUsername = memberRepository
                .existsByUsernameOrPhoneNumber(username, phoneNumber);

        // then
        assertThat(isExistsUsername).isTrue();
    }

    @Test
    @DisplayName("로그인 아이디 중복X")
    void notExistUsername() throws Exception {
        // given
        String username = memberDto.getUsername();
        String phoneNumber = memberDto.getPhoneNumber();

        // when
        boolean isExistsUsername = memberRepository
                .existsByUsernameOrPhoneNumber(username, phoneNumber);

        // then
        assertThat(isExistsUsername).isFalse();
    }

    @Test
    @DisplayName("휴대폰 번호 중복 검사")
    void existPhoneNumber() throws Exception {
        // given
        memberDto.setUsername("중복 방지");
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        String username = member.getUsername();
        String phoneNumber = member.getPhoneNumber();

        // when
        boolean isExistPhoneNumber = memberRepository
                .existsByUsernameOrPhoneNumber(username, phoneNumber);

        // then
        assertThat(isExistPhoneNumber).isTrue();
    }

    @Test
    @DisplayName("휴대폰 번호 중복X")
    void notExistPhoneNumber() throws Exception {
        String username = memberDto.getUsername();
        String phoneNumber = memberDto.getPhoneNumber();

        // when
        boolean isExistPhoneNumber = memberRepository
                .existsByUsernameOrPhoneNumber(username, phoneNumber);

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
        Member findMember = memberRepository
                .findByUsername(member.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(findMember.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 로그인 아이디로 삭제")
    void deleteByUsername() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);

        // when
        memberRepository.deleteByUsername(member.getUsername());
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size()).isEqualTo(0);
    }
}
