package com.handwoong.everyonewaiter.dto.category

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CategoryRequest(
    @field:NotNull(message = "{error.message.null}")
    @field:Size(min = 2, max = 20, message = "{error.message.size}")
    val name: String,
    val icon: String,
)
