package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.OnlyMsgResDto;
import com.handwoong.everyonewaiter.dto.member.MemberPwdReqDto;
import com.handwoong.everyonewaiter.dto.member.MemberReqDto;
import com.handwoong.everyonewaiter.dto.member.MemberResDto;
import java.util.List;

public interface MemberService {

    Long register(MemberReqDto memberDto);

    MemberResDto findMember(Long memberId);

    MemberResDto findMemberByUsername(String username);

    List<MemberResDto> findMemberList();

    OnlyMsgResDto changePassword(String username,
            MemberPwdReqDto passwordDto);

    OnlyMsgResDto deleteMember(String username, MemberPwdReqDto passwordDto);
}
