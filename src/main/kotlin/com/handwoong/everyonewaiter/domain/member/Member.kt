package com.handwoong.everyonewaiter.domain.member

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.member.MemberRole.ROLE_USER
import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY
import javax.validation.constraints.NotNull

@Entity
class Member(

    @NotNull
    @Column(unique = true)
    val username: String,

    @NotNull
    var password: String,

    @NotNull
    @Column(unique = true)
    val phoneNumber: String,

    var balance: Int = 300,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    var role: MemberRole = ROLE_USER,

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    val id: Long? = null,
) : BaseEntity() {

    fun encodePassword(encodePassword: String) {
        this.password = encodePassword
    }

    companion object {
        fun createMember(memberDto: MemberCreateRequest): Member {
            return Member(
                username = memberDto.username.lowercase(),
                password = memberDto.password,
                phoneNumber = memberDto.phoneNumber,
            )
        }
    }

}
