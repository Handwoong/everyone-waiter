package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.BasicResponseDto;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.member.MemberPasswordDto;
import com.handwoong.everyonewaiter.dto.member.MemberResponseDto;
import java.util.List;

public interface MemberService {

    Long register(MemberDto memberDto);

    MemberResponseDto findMember(Long memberId);

    MemberResponseDto findMemberByUsername(String username);

    List<MemberResponseDto> findMemberList();

    BasicResponseDto changePassword(String username,
            MemberPasswordDto passwordDto);

    BasicResponseDto deleteMember(String username, MemberPasswordDto passwordDto);
}
