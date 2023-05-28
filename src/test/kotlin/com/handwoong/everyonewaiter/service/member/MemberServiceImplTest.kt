package com.handwoong.everyonewaiter.service.member

import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.member.MemberRole
import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.member.PasswordRequest
import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MemberServiceImplTest @Autowired constructor(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Test
    @DisplayName("회원가입에 성공한다.")
    fun registerSuccessTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()

        // when
        memberService.register(memberCreateRequest)

        // then
        val memberList = memberRepository.findAll()
        assertThat(memberList).hasSize(1)
        assertThat(memberList[0].username).isEqualTo("username")
        assertThat(memberList[0].phoneNumber).isEqualTo("01012345678")
        assertThat(memberList[0].role).isEqualTo(MemberRole.ROLE_USER)
    }

    @Test
    @DisplayName("회원가입 시 로그인 아이디(username)가 이미 존재하면 예외가 발생한다.")
    fun registerExistsUsernameTest() {
        // given
        val memberCreateRequest1 = MemberCreateRequest.testOf()
        val memberCreateRequest2 = memberCreateRequest1.copy(phoneNumber = "01011112222")
        memberService.register(memberCreateRequest1)

        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.register(memberCreateRequest2)
        }.errorCode
        assertThat(errorCode).isEqualTo(USERNAME_OR_PHONE_EXISTS)
    }

    @Test
    @DisplayName("회원가입 시 휴대폰 번호(phoneNumber)가 이미 존재하면 예외가 발생한다.")
    fun registerExistsPhoneNumberTest() {
        // given
        val request1 = MemberCreateRequest.testOf()
        val request2 = request1.copy(username = "username")
        memberService.register(request1)

        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.register(request2)
        }.errorCode
        assertThat(errorCode).isEqualTo(USERNAME_OR_PHONE_EXISTS)
    }

    @Test
    @DisplayName("회원가입 시 비밀번호 암호화가 정상적으로 이루어진다.")
    fun registerEncodePasswordTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()

        // when
        memberService.register(memberCreateRequest)

        // then
        val memberList = memberRepository.findAll()
        assertThat(memberList[0].password).isNotEqualTo(memberCreateRequest.password)
        assertThat(memberList[0].password).contains("bcrypt")
    }

    @Test
    @DisplayName("회원 ID로 회원을 조회한다.")
    fun findMemberByIdTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()
        val member = Member.createMember(memberCreateRequest)
        val saveMember = memberRepository.save(member)

        // when
        val findMember = memberService.findMemberById(saveMember.id!!)

        // then
        assertThat(findMember.id).isEqualTo(saveMember.id)
        assertThat(findMember.username).isEqualTo("username")
        assertThat(findMember.phoneNumber).isEqualTo("01012345678")
        assertThat(findMember.balance).isEqualTo(300)
    }

    @Test
    @DisplayName("회원 ID로 회원 조회 시 회원이 존재하지 않으면 예외가 발생한다.")
    fun findMemberByIdNoSuchMemberTest() {
        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.findMemberById(1L)
        }.errorCode
        assertThat(errorCode).isEqualTo(ID_RESOURCE_NOT_FOUND)
    }

    @Test
    @DisplayName("회원 로그인 아이디(username)로 회원을 조회한다.")
    fun findMemberByUsernameTest() {
        // given
        val member = Member.createMember(MemberCreateRequest.testOf())
        val saveMember = memberRepository.save(member)

        // when
        val findMember = memberService.findMemberByUsername("username")

        // then
        assertThat(findMember.id).isEqualTo(saveMember.id)
        assertThat(findMember.username).isEqualTo("username")
        assertThat(findMember.phoneNumber).isEqualTo("01012345678")
        assertThat(findMember.balance).isEqualTo(300)
    }

    @Test
    @DisplayName("회원 로그인 아이디(username)로 조회 시 회원이 존재하지 않으면 예외가 발생한다.")
    fun findMemberByUsernameNoSuchMemberTest() {
        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.findMemberByUsername("username")
        }.errorCode
        assertThat(errorCode).isEqualTo(MEMBER_NOT_FOUND)
    }

    @Test
    @DisplayName("모든 회원 목록을 조회한다.")
    fun findAllMemberListTest() {
        // given
        memberRepository.saveAll(
            listOf(
                Member.createMember(MemberCreateRequest.testOf()),
                Member.createMember(MemberCreateRequest.testOf(username = "handwoong", phoneNumber = "01011112222")),
            )
        )

        // when
        val memberList = memberService.findAllMemberList()

        // then
        assertThat(memberList).hasSize(2)
        assertThat(memberList[0].username).isEqualTo("username")
        assertThat(memberList[1].username).isEqualTo("handwoong")
        assertThat(memberList[0].phoneNumber).isEqualTo("01012345678")
        assertThat(memberList[1].phoneNumber).isEqualTo("01011112222")
    }

    @Test
    @DisplayName("모든 회원 목록 조회 시 회원이 한명도 없다면 빈 리스트를 반환한다.")
    fun findAllMemberListNoSuchMemberTest() {
        // when
        val memberList = memberService.findAllMemberList()

        // then
        assertThat(memberList).isEmpty()
    }

    @Test
    @DisplayName("회원 비밀번호가 성공적으로 변경된다.")
    fun changePasswordSuccessTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()
        memberService.register(memberCreateRequest)
        val passwordRequest = PasswordRequest(
            currentPassword = "password1",
            newPassword = "newPassword1",
        )

        // when
        memberService.changePassword("username", passwordRequest)

        // then
        val memberList = memberRepository.findAll()
        assertThat(memberList[0].password).contains("bcrypt")
        assertThat(
            passwordEncoder.matches(
                passwordRequest.newPassword,
                memberList[0].password,
            )
        ).isTrue()
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 회원을 찾지 못하면 예외가 발생한다.")
    fun changePasswordNoSuchMemberTest() {
        // given
        val passwordRequest = PasswordRequest(
            currentPassword = "password1",
            newPassword = "newPassword1",
        )

        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.changePassword("username", passwordRequest)
        }.errorCode
        assertThat(errorCode).isEqualTo(MEMBER_NOT_FOUND)
    }

    @Test
    @DisplayName("회원 비밀번호 변경 시 현재 비밀번호와 저장된 비밀번호가 다르다면 예외가 발생한다.")
    fun changePasswordNotMatchPasswordTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()
        memberService.register(memberCreateRequest)
        val passwordRequest = PasswordRequest(
            // password1 != password2
            currentPassword = "password2",
            newPassword = "newPassword1",
        )

        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.changePassword("username", passwordRequest)
        }.errorCode
        assertThat(errorCode).isEqualTo(NOT_MATCH_PASSWORD)
    }

    @Test
    @DisplayName("회원 삭제가 정상 동작한다.")
    fun deleteMemberTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()
        memberService.register(memberCreateRequest)
        val passwordRequest = PasswordRequest(
            currentPassword = "password1",
            newPassword = "newPassword1",
        )

        // when
        memberService.deleteMember("username", passwordRequest)

        // then
        val memberList = memberRepository.findAll()
        assertThat(memberList).isEmpty()
    }

    @Test
    @DisplayName("회원 삭제 시 현재 비밀번호와 저장된 비밀번호가 다르다면 예외가 발생한다.")
    fun deleteMemberNotMatchPasswordTest() {
        // given
        val memberCreateRequest = MemberCreateRequest.testOf()
        memberService.register(memberCreateRequest)
        val passwordRequest = PasswordRequest(
            // password1 != password2
            currentPassword = "password2",
            newPassword = "newPassword1",
        )

        // when && then
        val errorCode = assertThrows<CustomException> {
            memberService.deleteMember("username", passwordRequest)
        }.errorCode
        assertThat(errorCode).isEqualTo(NOT_MATCH_PASSWORD)
    }

}
