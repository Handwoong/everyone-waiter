package com.handwoong.everyonewaiter.service.waiting

import com.handwoong.everyonewaiter.config.message.template.TemplateGenerator
import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingMessageStatus.*
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus.*
import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.repository.waiting.WaitingRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import response.AlimTalkSendResponse
import response.AlimTalkSendResponseMessage
import sender.MessageSender
import javax.persistence.EntityManager

@Transactional
@SpringBootTest
class WaitingServiceImplTest @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val waitingRepository: WaitingRepository,
    private val templateGenerator: TemplateGenerator,
    private val em: EntityManager,
) {

    private val messageSender = mockk<MessageSender>()
    private val waitingService =
        WaitingServiceImpl(storeRepository, waitingRepository, messageSender, templateGenerator)

    @Test
    @DisplayName("매장의 웨이팅 수를 조회한다.")
    fun findWaitingCountTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        store.addWaiting(Waiting.createWaiting(WaitingRegisterRequest.testOf(), store))
        store.addWaiting(Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 2, 1))

        // when
        val countResponse = waitingService.count("username", store.id!!)

        // then
        assertThat(countResponse.storeId).isEqualTo(store.id)
        assertThat(countResponse.count).isEqualTo(2)
    }

    @Test
    @DisplayName("매장의 웨이팅 수 조회 시 매장을 찾지 못하면 예외가 발생한다.")
    fun findWaitingCountNoSuchStoreTest() {
        // given
        memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))

        // when && then
        val errorCode = assertThrows<CustomException> {
            waitingService.count("username", 1L)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("웨이팅 ID로 웨이팅을 단건 조회한다.")
    fun findWaitingByIdTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val waiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        store.addWaiting(waiting)
        em.flush()

        // when
        val findWaiting = waitingService.findWaiting(waiting.id)

        // then
        assertThat(findWaiting.adult).isEqualTo(2)
        assertThat(findWaiting.children).isEqualTo(1)
        assertThat(findWaiting.number).isEqualTo(1)
        assertThat(findWaiting.turn).isEqualTo(0)
        assertThat(findWaiting.phoneNumber).isEqualTo("01012345678")
        assertThat(findWaiting.status).isEqualTo(WAIT)
        assertThat(findWaiting.messageStatus).isEqualTo(SEND_NOT)
    }

    @Test
    @DisplayName("웨이팅 상태가 대기 중인 웨이팅 목록을 조회한다.")
    fun findStatusWaitWaitingListTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val firstWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        firstWaiting.changeStatusNotWait(ENTER)
        store.addWaiting(firstWaiting)
        store.addWaiting(Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 2, 1))
        store.addWaiting(Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 3, 2))

        // when
        val waitingList = waitingService.findStatusWaitWaitingList("username", store.id!!)

        // then
        assertThat(waitingList.size).isEqualTo(2)
        for (waiting in waitingList) {
            assertThat(waiting.status).isEqualTo(WAIT)
        }
    }

    @Test
    @DisplayName("웨이팅 상태가 대기 중인 웨이팅 목록 조회 시 매장을 찾지 못하면 예외가 발생한다.")
    fun findStatusWaitWaitingListNoSuchStoreTest() {
        // given
        memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))

        // when && then
        val errorCode = assertThrows<CustomException> {
            waitingService.findStatusWaitWaitingList("username", 1L)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("웨이팅 등록 시 매장을 찾지 못하면 예외가 발생한다.")
    fun registerNoSuchStoreTest() {
        // given
        memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))

        // when && then
        val errorCode = assertThrows<CustomException> {
            waitingService.register("username", 1L, WaitingRegisterRequest.testOf())
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("웨이팅 등록 시 휴대폰 번호가 이미 등록되어 있다면 예외가 발생한다.")
    fun registerExistsPhoneNumberTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val waitingDto = WaitingRegisterRequest.testOf()
        val waiting = Waiting.createWaiting(waitingDto, store)
        store.addWaiting(waiting)

        // when && then
        val errorCode = assertThrows<CustomException> {
            waitingService.register("username", store.id!!, waitingDto)
        }.errorCode
        assertThat(errorCode).isEqualTo(PHONE_EXISTS)
    }

    @Test
    @DisplayName("웨이팅 등록에 성공한다.")
    fun registerSuccessTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when
        waitingService.register("username", store.id!!, WaitingRegisterRequest.testOf())

        // then
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].adult).isEqualTo(2)
        assertThat(waitingList[0].children).isEqualTo(1)
        assertThat(waitingList[0].number).isEqualTo(1)
        assertThat(waitingList[0].turn).isEqualTo(0)
        assertThat(waitingList[0].phoneNumber).isEqualTo("01012345678")
        assertThat(waitingList[0].status).isEqualTo(WAIT)
        assertThat(waitingList[0].messageStatus).isEqualTo(SEND_REGISTER)
    }

    @Test
    @DisplayName("웨이팅 등록 시 알림톡 전송에 실패하면 메시지 전송 상태는 변경되지 않는다.")
    fun registerFailSendAlimTalkTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse("F000")
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when
        waitingService.register("username", store.id!!, WaitingRegisterRequest.testOf())

        // then
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].messageStatus).isEqualTo(SEND_NOT)
    }

    @Test
    @DisplayName("웨이팅을 ENTER(입장) 상태로 변경한다.")
    fun enterWaitingTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val waiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        store.addWaiting(waiting)

        // when
        waitingService.enterWaiting("username", store.id!!, waiting.id)

        // then
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].status).isEqualTo(ENTER)
    }

    @Test
    @DisplayName("웨이팅을 ENTER(입장) 상태로 변경 시 대기중인 웨이팅의 순번이 1씩 감소한다.")
    fun enterWaitingDecreaseTurnAfterWaitingTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val firstWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        val secondWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 2, 1)
        val thirdWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 3, 2)
        store.addWaiting(firstWaiting)
        store.addWaiting(secondWaiting)
        store.addWaiting(thirdWaiting)

        // when
        waitingService.enterWaiting("username", store.id!!, firstWaiting.id)

        // then
        em.flush()
        em.clear()
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].turn).isEqualTo(-1)
        assertThat(waitingList[1].turn).isEqualTo(0)
        assertThat(waitingList[2].turn).isEqualTo(1)
    }

    @Test
    @DisplayName("웨이팅을 CANCEL(취소) 상태로 변경한다.")
    fun cancelWaitingTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val waiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        store.addWaiting(waiting)

        // when
        waitingService.cancelWaiting(store.id!!, waiting.id)

        // then
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].status).isEqualTo(CANCEL)
        assertThat(waitingList[0].messageStatus).isEqualTo(SEND_CANCEL)
    }

    @Test
    @DisplayName("웨이팅을 CANCEL(취소) 상태로 변경 시 대기중인 웨이팅 순번이 1씩 감소한다.")
    fun cancelWaitingDecreaseTurnAfterWaitingTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val firstWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        val secondWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 2, 1)
        val thirdWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 3, 2)
        store.addWaiting(firstWaiting)
        store.addWaiting(secondWaiting)
        store.addWaiting(thirdWaiting)

        // when
        waitingService.cancelWaiting(store.id!!, secondWaiting.id)

        // then
        em.flush()
        em.clear()
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].turn).isEqualTo(0)
        assertThat(waitingList[1].turn).isEqualTo(-1)
        assertThat(waitingList[2].turn).isEqualTo(1)
    }

    @Test
    @DisplayName("웨이팅 ID로 조회 후 ENTER(입장) 메시지를 전송한다.")
    fun findWaitingByIdAndSendEnterMessageTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val waiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        store.addWaiting(waiting)
        em.flush()

        // when
        waitingService.sendWaitingEnterMessage(waiting.id)

        // then
        val waitingList = waitingRepository.findAll()
        assertThat(waitingList[0].messageStatus).isEqualTo(SEND_ENTER)
    }

    @Test
    @DisplayName("첫번쨰 웨이팅 고객 ENTER(입장) 메시지 전송 시 3번째 웨이팅 고객이 있다면 READY(준비) 메시지를 전송한다.")
    fun thirdWaitingSendReadyMessageTest() {
        // given
        every { messageSender.sendAlimTalk(any()) } returns mockMessageResponse()
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))
        val firstWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store)
        val secondWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 2, 1)
        val thirdWaiting = Waiting.createWaiting(WaitingRegisterRequest.testOf(), store, 3, 2)
        store.addWaiting(firstWaiting)
        store.addWaiting(secondWaiting)
        store.addWaiting(thirdWaiting)
        em.flush()

        // when
        waitingService.sendWaitingEnterMessage(firstWaiting.id)

        // then
        val waitingList = waitingRepository.findAllWaiting(store.id!!, WAIT)
        assertThat(waitingList[0].messageStatus).isEqualTo(SEND_ENTER)
        assertThat(waitingList[1].messageStatus).isEqualTo(SEND_NOT)
        assertThat(waitingList[2].messageStatus).isEqualTo(SEND_READY)
    }

    private fun mockMessageResponse(requestStatusCode: String = "A000"): AlimTalkSendResponse {
        return AlimTalkSendResponse(
            requestId = "requestId",
            requestTime = "requestTime",
            statusCode = "statusCode",
            statusName = "statusName",
            messages = mutableListOf(
                AlimTalkSendResponseMessage(
                    messageId = "messageId",
                    to = "to",
                    requestStatusCode = requestStatusCode,
                    requestStatusDesc = "requestStatusDesc",
                    requestStatusName = "requestStatusName",
                    content = "content",
                    countryCode = "countryCode",
                    useSmsFailover = false,
                )
            ),
        )
    }

}
