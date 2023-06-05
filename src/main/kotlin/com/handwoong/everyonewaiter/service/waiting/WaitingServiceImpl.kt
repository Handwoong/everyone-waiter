package com.handwoong.everyonewaiter.service.waiting

import com.handwoong.everyonewaiter.config.message.template.TemplateGenerator
import com.handwoong.everyonewaiter.config.message.template.TemplateType
import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingMessageStatus.*
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus.*
import com.handwoong.everyonewaiter.dto.waiting.WaitingCountResponse
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.dto.waiting.WaitingResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.repository.waiting.WaitingRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import com.handwoong.everyonewaiter.util.logger
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sender.MessageSender
import java.util.*

@Service
@Transactional(readOnly = true)
class WaitingServiceImpl(
    private val storeRepository: StoreRepository,
    private val waitingRepository: WaitingRepository,
    private val messageSender: MessageSender,
    private val templateGenerator: TemplateGenerator,
) : WaitingService {

    private val log = logger()

    override fun count(username: String, storeId: Long): WaitingCountResponse {
        existsMemberStore(storeId, username)
        val waitingCount = waitingRepository.count(storeId, WAIT)
        return WaitingCountResponse.of(storeId, waitingCount)
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
    override fun register(username: String, storeId: Long, waitingDto: WaitingRegisterRequest): Int {
        existsMemberStore(storeId, username)
        existsPhoneNumber(waitingDto.phoneNumber)

        val findStore = storeRepository.findByIdOrThrow(storeId)
        val lastWaiting = waitingRepository.findLastWaiting(storeId, null)
        val statusWaitLastWaiting = waitingRepository.findLastWaiting(storeId, WAIT)
        val createWaiting =
            Waiting.createWaiting(waitingDto, findStore, lastWaiting, statusWaitLastWaiting)

        sendAlimTalk(TemplateType.REGISTER, createWaiting)
        waitingRepository.save(createWaiting)
        return createWaiting.number
    }

    @Transactional
    override fun enterWaiting(username: String, storeId: Long, waitingId: UUID) {
        existsMemberStore(storeId, username)
        val findWaiting = findLockWaitingById(waitingId)
        changeWaitingStatus(findWaiting, storeId, ENTER)
    }

    @Transactional
    override fun cancelWaiting(storeId: Long, waitingId: UUID) {
        val findWaiting = findLockWaitingById(waitingId)
        changeWaitingStatus(findWaiting, storeId, CANCEL)
        sendAlimTalk(TemplateType.CANCEL, findWaiting)
    }

    @Transactional
    override fun sendWaitingEnterMessage(waitingId: UUID) {
        val findWaiting = findWaitingById(waitingId)
        sendAlimTalk(TemplateType.ENTER, findWaiting)
        sendWaitingReadyMessage(findWaiting)
    }

    private fun sendWaitingReadyMessage(waiting: Waiting) {
        val waitingList = waitingRepository.findAllWaiting(waiting.store.id!!, WAIT)
        if (waitingList.size >= 3 && waitingList[0].id == waiting.id) {
            sendAlimTalk(TemplateType.READY, waiting)
        }
    }

    private fun existsPhoneNumber(phoneNumber: String) {
        if (!waitingRepository.existsPhoneNumber(phoneNumber)) {
            throwFail(PHONE_EXISTS)
        }
    }

    private fun existsMemberStore(storeId: Long, username: String) {
        storeRepository.findStore(storeId, username) ?: throwFail(STORE_NOT_FOUND)
    }

    private fun changeWaitingStatus(waiting: Waiting, storeId: Long, status: WaitingStatus) {
        waitingRepository.decreaseWaitingTurn(storeId, waiting.turn)
        waiting.changeStatusNotWait(status)
    }

    private fun findWaitingById(waitingId: UUID): Waiting {
        return waitingRepository.findByIdOrThrow(waitingId)
    }

    private fun findLockWaitingById(waitingId: UUID): Waiting {
        return waitingRepository.findLockWaitingById(waitingId) ?: throwFail(WAITING_NOT_FOUND)
    }

    private fun sendAlimTalk(templateType: TemplateType, waiting: Waiting) {
        log.info("[알림톡 전송] 템플릿 : ${templateType} 전화번호 : ${waiting.phoneNumber}")
        val response = messageSender.sendAlimTalk(templateGenerator.generate(templateType, waiting))

        val responseMessage = response.messages[0]
        if (responseMessage.requestStatusCode != "A000") {
            log.error("[알림톡 전송 실패] 코드 : ${responseMessage.requestStatusCode} 이유 : ${responseMessage.requestStatusDesc}")
            return
        }

        log.info("[알림톡 전송 요청 성공] 메시지 ID : ${responseMessage.messageId}")
        val sendType = when (templateType) {
            TemplateType.REGISTER -> SEND_REGISTER
            TemplateType.ENTER -> SEND_ENTER
            TemplateType.READY -> SEND_READY
            TemplateType.CANCEL -> SEND_CANCEL
        }
        waiting.changeMessageStatus(sendType)
    }

}
