package com.handwoong.everyonewaiter.service.waiting

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus.*
import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponse
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.STORE_NOT_FOUND
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.repository.waiting.WaitingRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class WaitingServiceImpl(
    private val storeRepository: StoreRepository,
    private val waitingRepository: WaitingRepository,
) : WaitingService {

    override fun count(username: String, storeId: Long): WaitingCountResponse {
        existsMemberStore(storeId, username)
        val waitingCount = waitingRepository.count(storeId, WAIT)
        return WaitingCountResponse.of(waitingCount)
    }

    override fun findWaiting(waitingId: UUID): WaitingResponse {
        val findWaiting = waitingRepository.findByIdOrThrow(waitingId)
        return WaitingResponse.of(findWaiting)
    }

    override fun findStatusWaitWaitingList(username: String, storeId: Long): List<WaitingResponse> {
        existsMemberStore(storeId, username)
        val waitingList = waitingRepository.findAllWaiting(storeId, WAIT)
        return waitingList.map(WaitingResponse::of)
    }

    @Transactional
    override fun register(username: String, storeId: Long, waitingDto: WaitingRegisterRequest) {
        existsMemberStore(storeId, username)

        val findStore = storeRepository.findByIdOrThrow(storeId)
        val lastWaiting = waitingRepository.findLastWaiting(storeId, null)
        val statusWaitLastWaiting = waitingRepository.findLastWaiting(storeId, WAIT)

        val createWaiting =
            Waiting.createWaiting(waitingDto, findStore, lastWaiting, statusWaitLastWaiting)
        waitingRepository.save(createWaiting)
    }

    @Transactional
    override fun enterWaiting(username: String, storeId: Long, waitingId: UUID) {
        existsMemberStore(storeId, username)
        findWaitingAndChangeStatus(waitingId, storeId, ENTER)
    }

    @Transactional
    override fun cancelWaiting(storeId: Long, waitingId: UUID) {
        findWaitingAndChangeStatus(waitingId, storeId, CANCEL)
    }

    private fun existsMemberStore(storeId: Long, username: String) {
        storeRepository.findStore(storeId, username) ?: throwFail(STORE_NOT_FOUND)
    }

    private fun findWaitingAndChangeStatus(waitingId: UUID, storeId: Long, status: WaitingStatus) {
        val findWaiting = waitingRepository.findByIdOrThrow(waitingId)
        waitingRepository.decreaseWaitingTurn(storeId, findWaiting.turn)
        findWaiting.changeStatusNotWait(status)
    }

}
