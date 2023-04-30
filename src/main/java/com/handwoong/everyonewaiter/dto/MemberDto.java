package com.handwoong.everyonewaiter.dto;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.utils.validation.DeleteValidationGroup;
import com.handwoong.everyonewaiter.utils.validation.UpdateValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDto {

    @Data
    @NoArgsConstructor
    public static class RequestDto {

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

        public RequestDto(String username, String password, String phoneNumber) {
            this.username = username.toLowerCase();
            this.password = password;
            this.phoneNumber = phoneNumber;
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ResponseDto {

        private Long id;

        private String username;

        private String phoneNumber;

        private int balance;

        public static ResponseDto from(Member member) {
            return ResponseDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .phoneNumber(member.getPhoneNumber())
                    .balance(member.getBalance())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PwdRequestDto {

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                groups = {UpdateValidationGroup.class, DeleteValidationGroup.class},
                message = "{error.message.password}")
        private String currentPassword;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                groups = {UpdateValidationGroup.class},
                message = "{error.message.password}")
        private String newPassword;

        public PwdRequestDto(String currentPassword, String newPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }
    }
}
