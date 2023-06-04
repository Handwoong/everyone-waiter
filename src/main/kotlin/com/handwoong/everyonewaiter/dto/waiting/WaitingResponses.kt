package com.handwoong.everyonewaiter.dto.waiting

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingMessageStatus
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus
import java.time.LocalDateTime
import java.util.*

data class WaitingResponse(
    val id: UUID,
    val number: Int,
    val turn: Int,
    val adult: Int,
    val children: Int,
    val phoneNumber: String,
    val status: WaitingStatus,
    val messageStatus: WaitingMessageStatus,
    val createdAt: LocalDateTime,
) {

    companion object {
        fun of(waiting: Waiting): WaitingResponse {
            return WaitingResponse(
                id = waiting.id,
                number = waiting.number,
                turn = waiting.turn,
                adult = waiting.adult,
                children = waiting.children,
                phoneNumber = waiting.phoneNumber,
                status = waiting.status,
                messageStatus = waiting.messageStatus,
                createdAt = waiting.createdAt,
            )
        }
    }

}

data class WaitingCountResponse(
    val storeId: Long,
    val count: Long,
) {

    companion object {
        fun of(
            storeId: Long,
            count: Long,
        ): WaitingCountResponse {
            return WaitingCountResponse(storeId, count)
        }
    }

}
