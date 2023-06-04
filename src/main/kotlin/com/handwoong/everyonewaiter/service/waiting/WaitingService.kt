package com.handwoong.everyonewaiter.service.waiting

import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponse
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponse
import java.util.*

interface WaitingService {

    fun count(username: String, storeId: Long): WaitingCountResponse

    fun findWaiting(waitingId: UUID): WaitingResponse

    fun findStatusWaitWaitingList(username: String, storeId: Long): List<WaitingResponse>

    fun register(username: String, storeId: Long, waitingDto: WaitingRegisterRequest): Int

    fun enterWaiting(username: String, storeId: Long, waitingId: UUID)

    fun cancelWaiting(storeId: Long, waitingId: UUID)

}
