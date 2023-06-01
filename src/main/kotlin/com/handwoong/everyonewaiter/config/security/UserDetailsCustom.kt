package com.handwoong.everyonewaiter.config.security

import com.handwoong.everyonewaiter.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UserDetailsCustom(
    val member: Member,
    authorities: MutableCollection<out GrantedAuthority>,
) : User(member.username, member.password, authorities)
