package com.handwoong.everyonewaiter.repository.waiting

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus


interface WaitingRepositoryCustom {

    fun count(
        storeId: Long,
        status: WaitingStatus?,
    ): Long

    fun existsPhoneNumber(
        phoneNumber: String,
    ): Boolean

    fun findLastWaiting(
        storeId: Long,
        status: WaitingStatus? = null,
    ): Waiting?

    fun findAllWaiting(
        storeId: Long,
        status: WaitingStatus?,
    ): List<Waiting>

    fun decreaseWaitingTurn(
        storeId: Long,
        turn: Int,
    )

}
