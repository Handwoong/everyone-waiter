package com.handwoong.everyonewaiter.dto.waiting

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class WaitingRegisterRequest(
    @field: Min(value = 1, message = "{error.message.min}")
    @field: Max(value = 20, message = "{error.message.max}")
    val adult: Int,

    @field: Min(value = 0, message = "{error.message.min}")
    @field: Max(value = 20, message = "{error.message.max}")
    val children: Int,

    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
        message = "{error.message.phoneNumber}"
    )
    val phoneNumber: String,
) {

    companion object {
        fun testOf(
            adult: Int = 2,
            children: Int = 1,
            phoneNumber: String = "01012345678",
        ): WaitingRegisterRequest {
            return WaitingRegisterRequest(
                adult = adult,
                children = children,
                phoneNumber = phoneNumber,
            )
        }
    }

}
