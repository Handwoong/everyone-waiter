package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.BasicResponseDto;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.member.MemberPasswordDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.exception.ResourceNotMatchException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberDto memberDto;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        memberDto = new MemberDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        when(memberRepository.existsByUsername(anyString())).thenReturn(false);
        when(memberRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        memberService.register(memberDto);

        // then
        verify(passwordEncoder, times(1)).encode(memberDto.getPassword());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 시 로그인 아이디 중복 예외")
    void registerExistsUsername() throws Exception {
        when(memberRepository.existsByUsername(anyString())).thenReturn(true);
        when(memberRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        assertThatThrownBy(() -> memberService.register(memberDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage("이미 존재하는 아이디 입니다.");
        verify(passwordEncoder, times(0)).encode(memberDto.getPassword());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 시 휴대폰 번호 중복 예외")
    void registerExistsPhoneNumber() throws Exception {
        when(memberRepository.existsByUsername(anyString())).thenReturn(false);
        when(memberRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThatThrownBy(() -> memberService.register(memberDto))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage("이미 존재하는 휴대폰 번호 입니다.");
        verify(passwordEncoder, times(0)).encode(memberDto.getPassword());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 ID 조회")
    void findMemberById() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberService.register(memberDto);
        when(memberRepository.findById(1L))
                .thenReturn(Optional.ofNullable(member));

        // when
        MemberResponseDto result = memberService.findMember(1L);

        // then
        assertThat(result.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(result.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(result.getBalance()).isEqualTo(300);
    }

    @Test
    @DisplayName("회원 존재하지 않는 ID 조회")
    void findNotExistsById() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberService.register(memberDto);

        assertThatThrownBy(() -> memberService.findMember(member.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("회원 Username 조회")
    void findMemberByUsername() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberService.register(memberDto);
        when(memberRepository.findByUsername(member.getUsername()))
                .thenReturn(Optional.of(member));

        // when
        MemberResponseDto result = memberService
                .findMemberByUsername(member.getUsername());

        // then
        assertThat(result.getUsername()).isEqualTo(memberDto.getUsername());
        assertThat(result.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        assertThat(result.getBalance()).isEqualTo(300);
    }

    @Test
    @DisplayName("회원 존재하지 않는 Username 조회")
    void findNotExistsByUsername() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        memberService.register(memberDto);

        assertThatThrownBy(() -> memberService.findMemberByUsername(member.getUsername()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("회원 목록 조회")
    void findMemberList() throws Exception {
        // given
        Member memberA = Member.createMember(memberDto);
        memberDto.setUsername("handwoong@test.com");
        memberDto.setPhoneNumber("01011112222");
        Member memberB = Member.createMember(memberDto);
        when(memberRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(memberA, memberB)));

        // when
        List<MemberResponseDto> memberList = memberService.findMemberList();
        MemberResponseDto memberDtoA = memberList.get(0);
        MemberResponseDto memberDtoB = memberList.get(1);

        // then
        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberDtoA.getUsername()).isEqualTo("handwoong");
        assertThat(memberDtoB.getUsername()).isEqualTo("handwoong@test.com");
    }

    @Test
    @DisplayName("회원 비밀번호 변경")
    void changeMemberPassword() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        MemberPasswordDto passwordDto = new MemberPasswordDto(member.getPassword(),
                "newPassword");
        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        BasicResponseDto result = memberService
                .changePassword(member.getUsername(), passwordDto);

        // then
        assertThat(member.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getMessage()).isEqualTo("success");
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 일치하지 않으면 예외 발생")
    void notMatchCurrentPassword() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        MemberPasswordDto passwordDto = new MemberPasswordDto(member.getPassword(),
                "newPassword");
        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(
                () -> memberService.changePassword(member.getUsername(), passwordDto))
                .isInstanceOf(ResourceNotMatchException.class)
                .hasMessage("기존 비밀번호가 일치하지 않습니다.");
        verify(passwordEncoder, times(0)).encode(anyString());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 회원을 찾을 수 없으면 예외 발생")
    void notFoundMember() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        MemberPasswordDto passwordDto = new MemberPasswordDto(member.getPassword(),
                "newPassword");

        assertThatThrownBy(
                () -> memberService.changePassword(member.getUsername(), passwordDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        MemberPasswordDto passwordDto = new MemberPasswordDto(member.getPassword(),
                "newPassword");
        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // when
        BasicResponseDto result = memberService.deleteMember(
                member.getUsername(), passwordDto);

        // then
        verify(memberRepository, times(1)).deleteById(member.getId());
        assertThat(result.getMessage()).isEqualTo("success");
    }

    @Test
    @DisplayName("회원 탈퇴 시 존재하지 않는 아이디면 예외 발생")
    void deleteMemberNotFound() throws Exception {
        // given
        MemberPasswordDto passwordDto = new MemberPasswordDto(memberDto.getPassword(),
                "newPassword");

        assertThatThrownBy(
                () -> memberService.deleteMember(memberDto.getUsername(), passwordDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
        verify(memberRepository, times(0)).deleteById(anyLong());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("회원 탈퇴 시 비밀번호가 일치하지 않으면 예외 발생")
    void deleteMemberNotMatchPassword() throws Exception {
        // given
        Member member = Member.createMember(memberDto);
        MemberPasswordDto passwordDto = new MemberPasswordDto(memberDto.getPassword(),
                "newPassword");
        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(member));

        assertThatThrownBy(
                () -> memberService.deleteMember(memberDto.getUsername(), passwordDto))
                .isInstanceOf(ResourceNotMatchException.class)
                .hasMessage("기존 비밀번호가 일치하지 않습니다.");
        verify(memberRepository, times(0)).deleteById(anyLong());
        verify(memberRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }
}
