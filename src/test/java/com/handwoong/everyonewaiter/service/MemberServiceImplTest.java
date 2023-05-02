package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.OnlyMsgResDto;
import com.handwoong.everyonewaiter.enums.MemberRole;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.FakeMemberRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceImplTest {

    private MemberServiceImpl memberService;

    private FakeMemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;

    private MemberDto.RequestDto memberDto;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        memberRepository = new FakeMemberRepository();
        memberService = new MemberServiceImpl(memberRepository, passwordEncoder);
        memberDto = new MemberDto.RequestDto("handwoong", "password1", "01012345678");
    }

    @AfterEach
    void afterEach() {
        memberRepository.clear();
    }

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        memberService.register(memberDto);
        Member member = memberRepository.findById(1L).orElseThrow();

        // then
        verify(passwordEncoder, times(1)).encode(memberDto.getPassword());
        assertThat(member.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(member.getPassword()).isEqualTo("encodedPassword");
        assertThat(member.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(member.getRole()).isEqualTo(MemberRole.ROLE_USER);
        assertThat(member.getBalance()).isEqualTo(300);
    }

    @Test
    @DisplayName("회원가입 시 로그인 아이디 중복 예외")
    void registerExistsUsername() throws Exception {
        // given
        memberService.register(memberDto);

        // when
        assertThatThrownBy(() -> memberService.register(memberDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("회원가입 시 휴대폰 번호 중복 예외")
    void registerExistsPhoneNumber() throws Exception {
        // given
        memberService.register(memberDto);
        memberDto.setUsername("username");

        // when
        assertThatThrownBy(() -> memberService.register(memberDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("회원 ID 조회")
    void findMemberById() throws Exception {
        // given
        memberService.register(memberDto);

        // when
        MemberDto.ResponseDto member = memberService.findMember(1L);

        // then
        assertThat(member.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(member.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(member.getBalance()).isEqualTo(300);
    }

    @Test
    @DisplayName("회원 존재하지 않는 ID 조회")
    void findNotExistsById() throws Exception {
        // given
        assertThatThrownBy(() -> memberService.findMember(1L))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
    }

    @Test
    @DisplayName("회원 Username 조회")
    void findMemberByUsername() throws Exception {
        // given
        memberService.register(memberDto);

        // when
        MemberDto.ResponseDto member = memberService.findMemberByUsername("handwoong");

        // then
        assertThat(member.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(member.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(member.getBalance()).isEqualTo(300);
    }

    @Test
    @DisplayName("회원 존재하지 않는 Username 조회")
    void findNotExistsByUsername() throws Exception {
        assertThatThrownBy(() -> memberService.findMemberByUsername("handwoong"))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
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
        List<MemberDto.ResponseDto> memberList = memberService.findMemberList();
        MemberDto.ResponseDto memberDtoA = memberList.get(0);
        MemberDto.ResponseDto memberDtoB = memberList.get(1);

        // then
        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberDtoA.getUsername()).isEqualTo("handwoong");
        assertThat(memberDtoB.getUsername()).isEqualTo("handwoong@test.com");
        assertThat(memberDtoA.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(memberDtoB.getPhoneNumber()).isEqualTo("01011112222");
    }

    @Test
    @DisplayName("회원 비밀번호 변경")
    void changeMemberPassword() throws Exception {
        // given
        memberDto.setPassword("encodedPassword");
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto(member.getPassword(), "newPassword");

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        OnlyMsgResDto result = memberService
                .changePassword(memberDto.getUsername(), passwordDto);

        // then
        assertThat(result.getMessage()).isEqualTo("success");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(passwordEncoder, times(1)).matches("encodedPassword", "encodedPassword");
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 일치하지 않으면 예외 발생")
    void notMatchCurrentPassword() throws Exception {
        // given
        memberDto.setPassword("encodedPassword");
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto("diffPassword", "newPassword");

        assertThatThrownBy(
                () -> memberService.changePassword(member.getUsername(), passwordDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
        verify(passwordEncoder, times(0)).encode(anyString());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 회원을 찾을 수 없으면 예외 발생")
    void notFoundMember() throws Exception {
        // given
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto("encodedPassword", "newPassword");

        assertThatThrownBy(
                () -> memberService.changePassword("handwoong", passwordDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto(member.getPassword(), "newPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // when
        OnlyMsgResDto result = memberService.deleteMember(member.getUsername(), passwordDto);
        long count = memberRepository.count();

        // then
        assertThat(result.getMessage()).isEqualTo("success");
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("회원 탈퇴 시 존재하지 않는 아이디면 예외 발생")
    void deleteMemberNotFound() throws Exception {
        // given
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto(memberDto.getPassword(), "newPassword");

        // when
        assertThatThrownBy(
                () -> memberService.deleteMember(memberDto.getUsername(), passwordDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");

        // then
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("회원 탈퇴 시 비밀번호가 일치하지 않으면 예외 발생")
    void deleteMemberNotMatchPassword() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        MemberDto.PwdRequestDto passwordDto =
                new MemberDto.PwdRequestDto(memberDto.getPassword(), "newPassword");

        // when
        assertThatThrownBy(
                () -> memberService.deleteMember(memberDto.getUsername(), passwordDto))
                .isInstanceOf(CustomException.class)
                .hasOnlyFields("errorCode");
        long count = memberRepository.count();

        // then
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        assertThat(count).isEqualTo(1);
    }
}
