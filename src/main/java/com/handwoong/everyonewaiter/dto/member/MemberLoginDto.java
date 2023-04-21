package com.handwoong.everyonewaiter.dto.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberLoginDto {

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;

    public MemberLoginDto(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
    }
}
