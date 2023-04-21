package com.handwoong.everyonewaiter.dto.member;

import com.handwoong.everyonewaiter.utils.validate.member.MemberLoginValidationGroup;
import com.handwoong.everyonewaiter.utils.validate.member.MemberRegisterValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRegisterDto {

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",
            groups = {MemberLoginValidationGroup.class,
                    MemberRegisterValidationGroup.class})
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            groups = {MemberLoginValidationGroup.class,
                    MemberRegisterValidationGroup.class})
    private String password;

    @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
            groups = MemberRegisterValidationGroup.class)
    private String phoneNumber;

    public MemberRegisterDto(String username, String password, String phoneNumber) {
        this.username = username.toLowerCase();
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
