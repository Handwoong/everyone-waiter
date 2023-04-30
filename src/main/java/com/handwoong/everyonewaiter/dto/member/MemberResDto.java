package com.handwoong.everyonewaiter.dto.member;

import com.handwoong.everyonewaiter.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberResDto {

    private Long id;

    private String username;

    private String phoneNumber;

    private int balance;

    public static MemberResDto from(Member member) {
        return MemberResDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .phoneNumber(member.getPhoneNumber())
                .balance(member.getBalance())
                .build();
    }
}
