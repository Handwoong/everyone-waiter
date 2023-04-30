package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.enums.ErrorCode.MEMBER_EXISTS;
import static com.handwoong.everyonewaiter.enums.ErrorCode.MEMBER_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.NOT_MATCH_PASSWORD;
import static com.handwoong.everyonewaiter.enums.ErrorCode.PHONE_NUMBER_EXISTS;

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
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
        isExistsUsername(memberDto);
        isExistsPhoneNumber(memberDto);

        // 회원 생성
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
        memberRepository.deleteById(member.getId());

        return new OnlyMsgResDto("success");
    }

    private void isExistsPhoneNumber(MemberDto.RequestDto memberDto) {
        boolean isExists = memberRepository.existsByPhoneNumber(memberDto.getPhoneNumber());

        if (isExists) {
            log.error("휴대폰 번호 중복 가입 요청 = 로그인 아이디 : '{}', 휴대폰 번호 : '{}'",
                    memberDto.getUsername(), memberDto.getPhoneNumber());
            throw new CustomException(PHONE_NUMBER_EXISTS);
        }
    }

    private void isExistsUsername(MemberDto.RequestDto memberDto) {
        boolean isExists = memberRepository.existsByUsername(memberDto.getUsername());

        if (isExists) {
            log.error("로그인 아이디 중복 가입 요청 = 로그인 아이디 : '{}'", memberDto.getUsername());
            throw new CustomException(MEMBER_EXISTS);
        }
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            log.error("[{}] 존재하지 않는 회원 찾기 = 찾으려는 아이디 : '{}'",
                    TransactionSynchronizationManager.getCurrentTransactionName(), memberId);
            return new CustomException(MEMBER_NOT_FOUND);
        });
    }

    private Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            log.error("[{}] 존재하지 않는 회원 찾기 = 찾으려는 로그인 아이디 : '{}'",
                    TransactionSynchronizationManager.getCurrentTransactionName(), username);
            return new CustomException(MEMBER_NOT_FOUND);
        });
    }

    private void matchPassword(Member member, String password) {
        if (passwordEncoder.matches(password, member.getPassword())) {
            return;
        }

        log.error("[{}] 기존 비밀번호가 일치하지 않음 = 로그인 아이디 : '{}'",
                TransactionSynchronizationManager.getCurrentTransactionName(),
                member.getUsername());
        throw new CustomException(NOT_MATCH_PASSWORD);
    }
}
