package com.handwoong.everyonewaiter.domain.store

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.store.StoreStatus.OPEN
import com.handwoong.everyonewaiter.dto.store.StoreRequest
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Store(

    var name: String,

    var telephone: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Embedded
    var businessTime: StoreBusinessTime,

    @Embedded
    var restTime: StoreRestTime,

    var status: StoreStatus = OPEN,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    val id: Long? = null,
) : BaseEntity() {

    fun update(storeDto: StoreRequest) {
        this.name = storeDto.name
        this.telephone = storeDto.telephone
        this.businessTime = StoreBusinessTime(storeDto.openTime, storeDto.closeTime)
        this.restTime = StoreRestTime(storeDto.startTime, storeDto.endTime)
    }

    companion object {
        fun createStore(
            storeDto: StoreRequest,
            member: Member,
        ): Store {
            return Store(
                name = storeDto.name,
                telephone = storeDto.telephone,
                member = member,
                businessTime = StoreBusinessTime(storeDto.openTime, storeDto.closeTime),
                restTime = StoreRestTime(storeDto.startTime, storeDto.endTime),
            )
        }
    }

}
