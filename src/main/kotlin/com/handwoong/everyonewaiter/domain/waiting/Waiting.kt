package com.handwoong.everyonewaiter.domain.waiting

import com.fasterxml.uuid.Generators
import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import java.util.*
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Waiting(

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    var number: Int,

    var turn: Int,

    val adult: Int,

    val children: Int,

    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    var status: WaitingStatus,

    @Enumerated(EnumType.STRING)
    var messageStatus: WaitingMessageStatus,
) : BaseEntity() {

    @Id
    @Column(name = "waiting_id", columnDefinition = "BINARY(16)")
    var id: UUID

    init {
        // UUID V1
        // Sequential UUID
        val uuid = Generators.timeBasedGenerator().generate()
        val uuidArray = uuid.toString().split("-")
        val uuidString = uuidArray[2] + uuidArray[1] + uuidArray[0] + uuidArray[3] + uuidArray[4]
        val sb = StringBuilder(uuidString)
        sb.insert(8, "-")
        sb.insert(13, "-")
        sb.insert(18, "-")
        sb.insert(23, "-")
        this.id = UUID.fromString(sb.toString())
    }

    fun statusEnterOrCancel(status: WaitingStatus) {
        if (status == WaitingStatus.WAIT) {
            return
        }
        this.status = status
        this.turn.dec()
    }

    fun statusSendMessageEnter() {
        this.messageStatus = WaitingMessageStatus.ENTER
    }

    fun statusSendMessageReady() {
        this.messageStatus = WaitingMessageStatus.READY
    }

    companion object {
        fun createWaiting(
            waitingDto: WaitingRegisterRequest,
            store: Store,
            lastWaiting: Waiting?,
            statusWaitLastWaiting: Waiting?,
        ): Waiting {
            val generateWaitingNumber = lastWaiting?.number?.inc() ?: 1
            val generateWaitingTurn = statusWaitLastWaiting?.turn?.inc() ?: 0

            return Waiting(
                store = store,
                number = generateWaitingNumber,
                turn = generateWaitingTurn,
                adult = waitingDto.adult,
                children = waitingDto.children,
                phoneNumber = waitingDto.phoneNumber,
                status = WaitingStatus.WAIT,
                messageStatus = WaitingMessageStatus.REGISTER,
            )
        }
    }

}
