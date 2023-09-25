package com.handwoong.everyonewaiter.config.security

import com.handwoong.everyonewaiter.exception.ErrorCode.MEMBER_NOT_FOUND
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceCustom(
    private val memberRepository: MemberRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findMember(username) ?: throwFail(MEMBER_NOT_FOUND)
        val roles = mutableListOf<GrantedAuthority>()
        roles.add(SimpleGrantedAuthority(member.role.name))
        return UserDetailsCustom(member, roles)
    }

}
