package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import java.util.List;

public interface MemberService {

    Long register(MemberDto memberDto);

    MemberResponseDto findMember(Long memberId);

    List<MemberResponseDto> findMemberList();
}
