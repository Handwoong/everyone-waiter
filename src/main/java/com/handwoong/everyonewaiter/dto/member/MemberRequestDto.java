package com.handwoong.everyonewaiter.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberRequestDto {

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$")
    private String phoneNumber;

    public MemberRequestDto(String email, String password, String name,
            String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
