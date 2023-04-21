package com.handwoong.everyonewaiter.domain;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.handwoong.everyonewaiter.dto.member.MemberRegisterDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String phoneNumber;

    private int balance;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Builder(access = PRIVATE)
    private Member(String username, String password, String phoneNumber, int balance,
            MemberRole role) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.role = role;
    }

    public static Member createMember(MemberRegisterDto memberDto) {
        return Member.builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .phoneNumber(memberDto.getPhoneNumber())
                .balance(0)
                .role(MemberRole.USER)
                .build();
    }

    public void encodePassword(String encodePassword) {
        this.password = encodePassword;
    }
}
