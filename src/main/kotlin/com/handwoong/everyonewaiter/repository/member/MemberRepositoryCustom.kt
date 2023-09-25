package com.handwoong.everyonewaiter.repository.member

import com.handwoong.everyonewaiter.domain.member.Member


interface MemberRepositoryCustom {

    fun existsMember(
        username: String,
        phoneNumber: String? = null,
    ): Boolean

    fun findMember(
        username: String,
    ): Member?

}
