package com.handwoong.everyonewaiter.service.store

import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.CleaningTestData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StoreServiceImplTest @Autowired constructor(
    private val storeService: StoreService,
    private val storeRepository: StoreRepository,
    private val memberRepository: MemberRepository,
) : CleaningTestData() {

    @Test
    @DisplayName("매장 등록에 성공한다.")
    fun registerSuccessTest() {
        // given
        memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val storeRequest = StoreRequest.testOf()

        // when
        storeService.register("username", storeRequest)

        // then
        val storeList = storeRepository.findAll()
        assertThat(storeList).hasSize(1)
        assertThat(storeList[0].name).isEqualTo("name")
        assertThat(storeList[0].telephone).isEqualTo("0551234567")
        assertThat(storeList[0].businessTime.openTime).isEqualTo("09:00")
        assertThat(storeList[0].businessTime.closeTime).isEqualTo("21:00")
        assertThat(storeList[0].restTime.startTime).isEqualTo("00:00")
        assertThat(storeList[0].restTime.endTime).isEqualTo("00:00")
    }

    @Test
    @DisplayName("매장 등록 시 회원을 찾지 못하면 예외가 발생한다.")
    fun registerNoSuchMemberTest() {
        // given
        val storeRequest = StoreRequest.testOf()

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.register("username", storeRequest)
        }.errorCode
        assertThat(errorCode).isEqualTo(MEMBER_NOT_FOUND)
    }

    @Test
    @DisplayName("매장 등록 시 자신의 매장 전화번호는 중복되어도 정상적으로 매장이 등록된다.")
    fun registerExistsMyTelephoneTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val storeRequest = StoreRequest.testOf()
        storeRepository.save(Store.createStore(storeRequest, member))

        // when
        storeService.register("username", storeRequest)

        // then
        val storeList = storeRepository.findAll()
        assertThat(storeList).hasSize(2)
        assertThat(storeList[0].telephone).isEqualTo(storeList[1].telephone)
    }

    @Test
    @DisplayName("매장 등록 시 매장 전화번호를 다른 회원의 매장에서 이미 사용중이면 예외가 발생한다.")
    fun registerExistsTelephoneTest() {
        // given
        val memberList = memberRepository.saveAll(
            listOf(
                Member.createMember(MemberCreateRequest.testOf()),
                Member.createMember(MemberCreateRequest.testOf(username = "handwoong", phoneNumber = "01011112222")),
            )
        )
        val storeRequest = StoreRequest.testOf()
        storeRepository.save(Store.createStore(storeRequest, memberList[0]))

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.register("handwoong", storeRequest)
        }.errorCode
        assertThat(errorCode).isEqualTo(TELEPHONE_EXISTS)
    }

    @Test
    @DisplayName("매장 정보 수정이 정상적으로 동작 한다.")
    fun updateSuccessTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when
        storeService.update("username", store.id!!, StoreRequest.testOf(name = "store", telephone = "021112222"))

        // then
        val storeList = storeRepository.findAll()
        assertThat(storeList).hasSize(1)
        assertThat(storeList[0].name).isEqualTo("store")
        assertThat(storeList[0].telephone).isEqualTo("021112222")
    }

    @Test
    @DisplayName("매장 정보 수정 시 매장 전화번호를 다른 회원의 매장에서 이미 사용중이면 예외가 발생한다.")
    fun updateExistsTelephoneTest() {
        // given
        val memberList = memberRepository.saveAll(
            listOf(
                Member.createMember(MemberCreateRequest.testOf()),
                Member.createMember(MemberCreateRequest.testOf(username = "handwoong", phoneNumber = "01011112222")),
            )
        )
        val storeList = storeRepository.saveAll(
            listOf(
                Store.createStore(StoreRequest.testOf(), memberList[0]),
                Store.createStore(StoreRequest.testOf(telephone = "021112222"), memberList[1]),
            )
        )

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.update("username", storeList[0].id!!, StoreRequest.testOf(telephone = "021112222"))
        }.errorCode
        assertThat(errorCode).isEqualTo(TELEPHONE_EXISTS)
    }

}
