package com.handwoong.everyonewaiter.dto.member

import com.handwoong.everyonewaiter.domain.member.Member

data class MemberResponse(
    val id: Long,
    val username: String,
    val phoneNumber: String,
    val balance: Int,
) {

    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                username = member.username,
                phoneNumber = member.phoneNumber,
                balance = member.balance,
            )
        }
    }

}

data class TokenResponse(
    val grantType: String,
    val accessToken: String,
) {

    companion object {
        fun of(accessToken: String): TokenResponse {
            return TokenResponse(
                grantType = "Cookie",
                accessToken = accessToken,
            )
        }
    }

}
