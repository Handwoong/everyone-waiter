package com.handwoong.everyonewaiter.repository.member

import com.handwoong.everyonewaiter.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom
