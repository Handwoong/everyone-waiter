package com.handwoong.everyonewaiter.dto.store

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class StoreRequest(
    @field: NotNull(message = "{error.message.null}")
    @field: Size(min = 2, max = 50, message = "{error.message.size}")
    val name: String,

    @field: NotNull(message = "{error.message.null}")
    @field: Pattern(
        regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$",
        message = "{error.message.telNumber}",
    )
    val telephone: String,

    @field: NotNull(message = "{error.message.null}")
    @field: DateTimeFormat(pattern = "HH:mm")
    val startTime: LocalTime,

    @field: NotNull(message = "{error.message.null}")
    @field: DateTimeFormat(pattern = "HH:mm")
    val endTime: LocalTime,

    @field: NotNull(message = "{error.message.null}")
    @field: DateTimeFormat(pattern = "HH:mm")
    val openTime: LocalTime,

    @field: NotNull(message = "{error.message.null}")
    @field: DateTimeFormat(pattern = "HH:mm")
    val closeTime: LocalTime,
) {

    companion object {
        fun testOf(
            name: String = "name",
            telephone: String = "0551234567",
            startTime: LocalTime = LocalTime.of(0, 0),
            endTime: LocalTime = LocalTime.of(0, 0),
            openTime: LocalTime = LocalTime.of(9, 0),
            closeTime: LocalTime = LocalTime.of(21, 0),
        ): StoreRequest {
            return StoreRequest(
                name = name,
                telephone = telephone,
                startTime = startTime,
                endTime = endTime,
                openTime = openTime,
                closeTime = closeTime,
            )
        }
    }

}
