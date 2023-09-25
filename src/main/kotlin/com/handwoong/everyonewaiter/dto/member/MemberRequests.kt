package com.handwoong.everyonewaiter.dto.member

import com.handwoong.everyonewaiter.util.DeleteValidationGroup
import com.handwoong.everyonewaiter.util.UpdateValidationGroup
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class MemberCreateRequest(
    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^[A-Za-z0-9]{6,20}$",
        message = "{error.message.username}"
    )
    val username: String,

    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "{error.message.password}",
    )
    val password: String,

    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
        message = "{error.message.phoneNumber}",
    )
    val phoneNumber: String,
) {

    companion object {
        fun testOf(
            username: String = "username",
            password: String = "password1",
            phoneNumber: String = "01012345678",
        ): MemberCreateRequest {
            return MemberCreateRequest(
                username = username,
                password = password,
                phoneNumber = phoneNumber,
            )
        }
    }

}

data class MemberLoginRequest(
    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^[A-Za-z0-9]{6,20}$",
        message = "{error.message.username}"
    )
    val username: String,

    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "{error.message.password}",
    )
    val password: String,
) {

    companion object {
        fun testOf(
            username: String = "username",
            password: String = "password1",
        ): MemberLoginRequest {
            return MemberLoginRequest(
                username = username,
                password = password,
            )
        }
    }

}

data class PasswordRequest(
    @field: Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        groups = [UpdateValidationGroup::class, DeleteValidationGroup::class],
        message = "{error.message.password}",
    )
    val currentPassword: String,

    @field: Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        groups = [UpdateValidationGroup::class],
        message = "{error.message.password}",
    )
    val newPassword: String,
)
