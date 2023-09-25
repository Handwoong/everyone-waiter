package com.handwoong.everyonewaiter.service.member

import com.handwoong.everyonewaiter.config.security.JwtTokenProvider
import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.dto.member.*
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) : MemberService {

    override fun login(memberDto: MemberLoginRequest): TokenResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(memberDto.username, memberDto.password)
        val authenticate = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        return jwtTokenProvider.generateToken(authenticate)
    }

    @Transactional
    override fun register(memberDto: MemberCreateRequest) {
        existsUsernameOrPhoneNumber(
            username = memberDto.username,
            phoneNumber = memberDto.phoneNumber,
        )

        val createMember = Member.createMember(memberDto)
        createMember.encodePassword(passwordEncoder.encode(createMember.password))
        memberRepository.save(createMember)
    }

    override fun findMemberById(memberId: Long): MemberResponse {
        val findMember = memberRepository.findByIdOrThrow(memberId)
        return MemberResponse.of(findMember)
    }

    override fun findMemberByUsername(username: String): MemberResponse {
        val findMember = findByUsername(username)
        return MemberResponse.of(findMember)
    }

    override fun findAllMemberList(): List<MemberResponse> {
        val memberList = memberRepository.findAll()
        return memberList.map { member -> MemberResponse.of(member) }
    }

    @Transactional
    override fun changePassword(username: String, passwordDto: PasswordRequest) {
        val findMember = findByUsername(username)
        matchPassword(passwordDto.currentPassword, findMember.password)
        findMember.encodePassword(passwordEncoder.encode(passwordDto.newPassword))
    }

    @Transactional
    override fun deleteMember(username: String, passwordDto: PasswordRequest) {
        val findMember = findByUsername(username)
        matchPassword(passwordDto.currentPassword, findMember.password)
        memberRepository.delete(findMember)
    }

    private fun existsUsernameOrPhoneNumber(username: String, phoneNumber: String? = null) {
        if (memberRepository.existsMember(username, phoneNumber)) {
            throwFail(USERNAME_OR_PHONE_EXISTS)
        }
    }

    private fun findByUsername(username: String): Member {
        return memberRepository.findMember(username) ?: throwFail(MEMBER_NOT_FOUND)
    }

    private fun matchPassword(userInputPassword: String, currentPassword: String) {
        if (!passwordEncoder.matches(userInputPassword, currentPassword)) {
            throwFail(NOT_MATCH_PASSWORD)
        }
    }

}
