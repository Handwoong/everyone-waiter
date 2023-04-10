package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.member.MemberRequestDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import java.util.List;

public interface MemberService {

    Long register(MemberRequestDto memberDto);

    MemberResponseDto findMember(Long memberId);

    List<MemberResponseDto> findMemberList();
}
