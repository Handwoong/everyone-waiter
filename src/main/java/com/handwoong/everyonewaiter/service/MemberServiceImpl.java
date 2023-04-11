package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.dto.member.MemberRequestDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long register(MemberRequestDto memberDto) {
        isExistsEmail(memberDto);
        isExistsPhoneNumber(memberDto);

        // 회원 생성
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        log.info("회원 생성 = 아이디 : '{}', 이메일 : '{}', 이름 : '{}', 휴대폰 번호 : '{}'",
                member.getId(), member.getEmail(), member.getName(),
                member.getPhoneNumber());
        return member.getId();
    }

    private void isExistsPhoneNumber(MemberRequestDto memberDto) {
        boolean isExistsPhoneNumber = memberRepository.existsByPhoneNumber(
                memberDto.getPhoneNumber());
        if (isExistsPhoneNumber) {
            log.error("휴대폰 번호 중복 가입 요청 = 이메일 : '{}', 휴대폰 번호 : '{}'",
                    memberDto.getEmail(), memberDto.getPhoneNumber());
            throw new ResourceExistsException("이미 존재하는 휴대폰 번호 입니다.");
        }
    }

    private void isExistsEmail(MemberRequestDto memberDto) {
        boolean isExistsEmail = memberRepository.existsByEmail(memberDto.getEmail());
        if (isExistsEmail) {
            log.error("이메일 중복 가입 요청 = 이메일 : '{}'", memberDto.getEmail());
            throw new ResourceExistsException("이미 존재하는 이메일 입니다.");
        }
    }

    @Override
    public MemberResponseDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원 찾기 = 찾으려는 아이디 : '{}'", memberId);
                    return new ResourceNotFoundException("존재하지 않는 회원입니다.");
                });
        MemberResponseDto memberDto = MemberResponseDto.from(member);
        log.info("회원 조회 = 아이디 : '{}', 이메일 : '{}', 이름 : '{}', 잔액 : '{}'",
                memberDto.getId(), memberDto.getEmail(), memberDto.getName(),
                memberDto.getBalance());
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
}
