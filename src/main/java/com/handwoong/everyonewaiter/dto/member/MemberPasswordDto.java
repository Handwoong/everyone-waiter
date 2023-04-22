package com.handwoong.everyonewaiter.dto.member;

import com.handwoong.everyonewaiter.utils.validation.DeleteValidationGroup;
import com.handwoong.everyonewaiter.utils.validation.UpdateValidationGroup;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberPasswordDto {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            groups = {UpdateValidationGroup.class, DeleteValidationGroup.class},
            message = "{error.message.password}")
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            groups = {UpdateValidationGroup.class},
            message = "{error.message.password}")
    private String newPassword;

    public MemberPasswordDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
