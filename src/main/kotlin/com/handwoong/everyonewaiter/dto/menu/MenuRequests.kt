package com.handwoong.everyonewaiter.dto.menu

import com.handwoong.everyonewaiter.domain.menu.MenuStatus
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class MenuRequest(
    @field:NotNull(message = "{error.message.null}")
    @field:Size(min = 2, max = 20, message = "{error.message.size}")
    val name: String,

    val description: String? = null,
    val notice: String? = null,

    @field:NotNull(message = "{error.message.null}")
    val price: Int,

    @field:NotNull(message = "{error.message.null}")
    val status: MenuStatus = MenuStatus.BASIC,
    val image: String? = null,

    @field:NotNull(message = "{error.message.null}")
    @field:Min(value = 0, message = "{error.message.min}")
    @field:Max(value = 10, message = "{error.message.max}")
    val spicy: Int,

    @field:NotNull(message = "{error.message.null}")
    val sortingSequence: Int,
)
