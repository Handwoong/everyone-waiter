package com.handwoong.everyonewaiter.dto.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",
            message = "{error.message.username}")
    private String username;

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "{error.message.password}")
    private String password;

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
            message = "{error.message.phoneNumber}")
    private String phoneNumber;

    public MemberDto(String username, String password, String phoneNumber) {
        this.username = username.toLowerCase();
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
