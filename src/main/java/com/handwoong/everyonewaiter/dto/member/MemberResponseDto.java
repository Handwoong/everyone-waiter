package com.handwoong.everyonewaiter.dto.member;

import com.handwoong.everyonewaiter.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberResponseDto {

    private Long id;

    private String email;

    private String name;

    private int balance;

    public static MemberResponseDto from(Member member) {
        Assert.notNull(member, "(null) 회원 엔티티를 responseDto 변환에 실패하였습니다.");
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .balance(member.getBalance())
                .build();
    }
}
