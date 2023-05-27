package com.handwoong.everyonewaiter.repository.member

import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.member.QMember.member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : MemberRepositoryCustom {

    override fun existsMember(
        username: String,
        phoneNumber: String?,
    ): Boolean {
        val findMember = queryFactory.select(member)
            .from(member)
            .where(
                member.username.eq(username)
                    .or(phoneNumber?.let { member.phoneNumber.eq(phoneNumber) })
            )
            .fetchFirst()
        return findMember != null
    }

    override fun findMember(
        username: String,
    ): Member? {
        return queryFactory.select(member)
            .from(member)
            .where(
                member.username.eq(username),
            )
            .fetchFirst()
    }

}
