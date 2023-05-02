package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.enums.ErrorCode.MEMBER_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.MEMBER_OR_PHONE_EXISTS;
import static com.handwoong.everyonewaiter.enums.ErrorCode.NOT_MATCH_PASSWORD;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.OnlyMsgResDto;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long register(MemberDto.RequestDto memberDto) {
        String username = memberDto.getUsername();
        String phoneNumber = memberDto.getPhoneNumber();

        isExistsUsernameOrPhone(username, phoneNumber);

        Member member = Member.createMember(memberDto);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        return member.getId();
    }

    @Override
    public MemberDto.ResponseDto findMember(Long memberId) {
        Member member = findById(memberId);

        return MemberDto.ResponseDto.from(member);
    }

    @Override
    public MemberDto.ResponseDto findMemberByUsername(String username) {
        Member member = findByUsername(username);

        return MemberDto.ResponseDto.from(member);
    }

    @Override
    public List<MemberDto.ResponseDto> findMemberList() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberDto.ResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public OnlyMsgResDto changePassword(String username, MemberDto.PwdRequestDto passwordDto) {
        Member member = findByUsername(username);
        matchPassword(member, passwordDto.getCurrentPassword());
        member.encodePassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        return new OnlyMsgResDto("success");
    }

    @Override
    @Transactional
    public OnlyMsgResDto deleteMember(String username, MemberDto.PwdRequestDto passwordDto) {
        Member member = findByUsername(username);
        matchPassword(member, passwordDto.getCurrentPassword());
        memberRepository.deleteByUsername(member.getUsername());

        return new OnlyMsgResDto("success");
    }

    private void isExistsUsernameOrPhone(String username, String phoneNumber) {
        boolean isExists = memberRepository.existsByUsernameOrPhoneNumber(username, phoneNumber);

        if (isExists) {
            throw new CustomException(MEMBER_OR_PHONE_EXISTS);
        }
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    private Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    private void matchPassword(Member member, String password) {
        boolean isCorrectPassword = passwordEncoder.matches(password, member.getPassword());

        if (!isCorrectPassword) {
            throw new CustomException(NOT_MATCH_PASSWORD);
        }
    }
}
