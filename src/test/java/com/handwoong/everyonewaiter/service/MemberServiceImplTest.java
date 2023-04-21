package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.member.MemberRegisterDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    private MemberRegisterDto memberDto;


    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRegisterDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        Long memberId = memberService.register(memberDto);

        assertThat(memberId).isOne();
    }

    @Test
    @DisplayName("회원가입 시 로그인 아이디 중복 예외")
    void registerExistsUsername() throws Exception {
        memberService.register(memberDto);
        Assertions.assertThrows(ResourceExistsException.class,
                () -> memberService.register(memberDto));
    }

    @Test
    @DisplayName("회원가입 시 휴대폰 번호 중복 예외")
    void registerExistsPhoneNumber() throws Exception {
        memberService.register(memberDto);
        memberDto.setUsername("username");
        Assertions.assertThrows(ResourceExistsException.class,
                () -> memberService.register(memberDto));
    }

    @Test
    @DisplayName("회원 ID 조회")
    void findMemberById() throws Exception {
        // given
        Long memberId = memberService.register(memberDto);

        // when
        MemberResponseDto memberResponseDto = memberService.findMember(memberId);

        // then
        assertThat(memberResponseDto.getId()).isEqualTo(memberId);
        assertThat(memberResponseDto.getUsername()).isEqualTo(memberDto.getUsername());
    }

    @Test
    @DisplayName("회원 목록 조회")
    void findMemberList() throws Exception {
        // given
        memberService.register(memberDto);
        memberDto.setUsername("handwoong@test.com");
        memberDto.setPhoneNumber("01011112222");
        memberService.register(memberDto);

        // when
        List<MemberResponseDto> memberList = memberService.findMemberList();
        MemberResponseDto memberA = memberList.get(0);
        MemberResponseDto memberB = memberList.get(1);

        // then
        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberA.getUsername()).isEqualTo("handwoong");
        assertThat(memberB.getUsername()).isEqualTo("handwoong@test.com");
    }
}
