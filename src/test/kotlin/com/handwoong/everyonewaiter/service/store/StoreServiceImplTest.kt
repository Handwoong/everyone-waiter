package com.handwoong.everyonewaiter.service.store

import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class StoreServiceImplTest @Autowired constructor(
    private val storeService: StoreService,
    private val storeRepository: StoreRepository,
    private val memberRepository: MemberRepository,
) {

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

    @Test
    @DisplayName("매장 정보 수정 시 로그인 아이디(username) 및 매장 ID로 매장을 찾지 못하면 예외가 발생한다.")
    fun updateNoSuchMemberTest() {
        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.update("username", 1L, StoreRequest.testOf())
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("매장 삭제가 정상적으로 동작한다.")
    fun deleteSuccessTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when
        storeService.delete("username", store.id!!)

        // then
        val storeList = storeRepository.findAll()
        assertThat(storeList).hasSize(0)
    }

    @Test
    @DisplayName("매장 삭제 시 잘못된 매장 ID로 인해 매장을 찾지 못하면 예외가 발생한다.")
    fun deleteNoSuchStoreByStoreIdTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.delete("username", 999L)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("매장 삭제 시 잘못된 회원 로그인 아이디(username)으로 인해 매장을 찾지 못하면 예외가 발생한다.")
    fun deleteNoSuchStoreByUsernameTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.delete("username2", store.id!!)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("로그인 아이디(username)과 매장 ID로 매장을 단건 조회한다.")
    fun findStoreTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when
        val findStore = storeService.findStore("username", store.id!!)

        // then
        assertThat(findStore.name).isEqualTo("name")
        assertThat(findStore.telephone).isEqualTo("0551234567")
        assertThat(findStore.businessTime.openTime).isEqualTo("09:00")
        assertThat(findStore.businessTime.closeTime).isEqualTo("21:00")
        assertThat(findStore.restTime.startTime).isEqualTo("00:00")
        assertThat(findStore.restTime.endTime).isEqualTo("00:00")
    }

    @Test
    @DisplayName("매장 단건 조회 시 잘못된 매장 ID로 인해 매장을 찾지 못하면 예외가 발생한다.")
    fun findStoreNoSuchStoreByStoreIdTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.findStore("username", 999L)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("매장 단건 조회 시 잘못된 회원 로그인 아이디(username)으로 인해 매장을 찾지 못하면 예외가 발생한다.")
    fun findStoreNoSuchStoreByUsernameTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        val store = storeRepository.save(Store.createStore(StoreRequest.testOf(), member))

        // when && then
        val errorCode = assertThrows<CustomException> {
            storeService.findStore("username2", store.id!!)
        }.errorCode
        assertThat(errorCode).isEqualTo(STORE_NOT_FOUND)
    }

    @Test
    @DisplayName("회원의 매장 목록을 조회한다.")
    fun findStoreListTest() {
        // given
        val member = memberRepository.save(Member.createMember(MemberCreateRequest.testOf()))
        storeRepository.saveAll(
            listOf(
                Store.createStore(StoreRequest.testOf(), member),
                Store.createStore(StoreRequest.testOf(), member),
            )
        )

        // when
        val findStoreList = storeService.findStoreList("username")

        // then
        assertThat(findStoreList).hasSize(2)
    }

}
