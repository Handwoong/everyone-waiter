package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.BasicResponseDto;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.member.MemberPasswordDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.exception.ResourceNotMatchException;
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
    public Long register(MemberDto memberDto) {
        isExistsUsername(memberDto);
        isExistsPhoneNumber(memberDto);

        // 회원 생성
        Member member = Member.createMember(memberDto);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        log.info("회원 생성 = 아이디 : '{}', 로그인 아이디 : '{}', 휴대폰 번호 : '{}'",
                member.getId(), member.getUsername(), member.getPhoneNumber());
        return member.getId();
    }

    @Override
    public MemberResponseDto findMember(Long memberId) {
        Member member = findById(memberId);
        return MemberResponseDto.from(member);
    }

    @Override
    public MemberResponseDto findMemberByUsername(String username) {
        Member member = findByUsername(username);
        MemberResponseDto memberDto = MemberResponseDto.from(member);

        log.info("회원 조회 = 아이디 : '{}', 로그인 아이디 : '{}', 잔액 : '{}'",
                memberDto.getId(), memberDto.getUsername(), memberDto.getBalance());
        return memberDto;
    }


    @Override
    public List<MemberResponseDto> findMemberList() {
        List<Member> members = memberRepository.findAll();

        log.info("회원 전체 목록 조회 = '{}'명", members.size());
        return members.stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public BasicResponseDto changePassword(String username,
            MemberPasswordDto passwordDto) {
        Member member = findByUsername(username);
        matchPassword(member, passwordDto.getCurrentPassword());
        member.encodePassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        log.info("비밀번호 변경 성공 = 로그인 아이디 : '{}'", username);
        return new BasicResponseDto("success");
    }

    @Override
    @Transactional
    public BasicResponseDto deleteMember(String username,
            MemberPasswordDto passwordDto) {
        Member member = findByUsername(username);
        matchPassword(member, passwordDto.getCurrentPassword());
        memberRepository.deleteById(member.getId());

        log.info("회원 삭제 성공 = 로그인 아이디 : '{}'", username);
        return new BasicResponseDto("success");
    }

    private void isExistsPhoneNumber(MemberDto memberDto) {
        boolean isExistsPhoneNumber = memberRepository.existsByPhoneNumber(
                memberDto.getPhoneNumber());

        if (isExistsPhoneNumber) {
            log.error("휴대폰 번호 중복 가입 요청 = 로그인 아이디 : '{}', 휴대폰 번호 : '{}'",
                    memberDto.getUsername(), memberDto.getPhoneNumber());
            throw new ResourceExistsException("이미 존재하는 휴대폰 번호 입니다.");
        }
    }

    private void isExistsUsername(MemberDto memberDto) {
        boolean isExistsUsername = memberRepository.existsByUsername(
                memberDto.getUsername());

        if (isExistsUsername) {
            log.error("로그인 아이디 중복 가입 요청 = 로그인 아이디 : '{}'", memberDto.getUsername());
            throw new ResourceExistsException("이미 존재하는 아이디 입니다.");
        }
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("[{}] 존재하지 않는 회원 찾기 = 찾으려는 아이디 : '{}'",
                            TransactionSynchronizationManager.getCurrentTransactionName(),
                            memberId);
                    return new ResourceNotFoundException("존재하지 않는 회원입니다.");
                });
    }

    private Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("[{}] 존재하지 않는 회원 찾기 = 찾으려는 로그인 아이디 : '{}'",
                            TransactionSynchronizationManager.getCurrentTransactionName(),
                            username);
                    return new ResourceNotFoundException("존재하지 않는 회원입니다.");
                });
    }

    private void matchPassword(Member member, String password) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.error("[{}] 기존 비밀번호가 일치하지 않음 = 로그인 아이디 : '{}'",
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    member.getUsername());
            throw new ResourceNotMatchException("기존 비밀번호가 일치하지 않습니다.");
        }
    }
}
