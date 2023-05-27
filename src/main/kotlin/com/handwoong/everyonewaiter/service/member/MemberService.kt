package com.handwoong.everyonewaiter.service.member

import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.member.MemberResponse
import com.handwoong.everyonewaiter.dto.member.PasswordRequest

interface MemberService {

    fun register(memberDto: MemberCreateRequest)

    fun findMemberById(memberId: Long): MemberResponse

    fun findMemberByUsername(username: String): MemberResponse

    fun findAllMemberList(): List<MemberResponse>

    fun changePassword(username: String, passwordDto: PasswordRequest)

    fun deleteMember(username: String, passwordDto: PasswordRequest)

}
