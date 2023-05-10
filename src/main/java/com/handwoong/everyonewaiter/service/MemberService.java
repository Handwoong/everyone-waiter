package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.OnlyMsgResDto;
import java.util.List;

public interface MemberService {

    Long register(MemberDto.RequestDto memberDto);

    MemberDto.ResponseDto findMember(Long memberId);

    MemberDto.ResponseDto findMemberByUsername(String username);

    List<MemberDto.ResponseDto> findMemberList();

    OnlyMsgResDto changePassword(String username, MemberDto.PwdRequestDto passwordDto);

    OnlyMsgResDto deleteMember(String username, MemberDto.PwdRequestDto passwordDto);
}
