package com.handwoong.everyonewaiter.domain.waiting

import com.fasterxml.uuid.Generators
import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.domain.waiting.WaitingMessageStatus.*
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus.*
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

    @Id
    @Column(name = "waiting_id", columnDefinition = "BINARY(16)")
    val id: UUID,
) : BaseEntity() {

    fun changeStatusNotWait(status: WaitingStatus) {
        if (status != WAIT) {
            this.status = status
            this.turn--
        }
    }

    fun changeMessageStatusNotRegister(status: WaitingMessageStatus) {
        if (status != SEND_REGISTER) {
            this.messageStatus = status
        }
    }

    companion object {
        fun createWaiting(
            waitingDto: WaitingRegisterRequest,
            store: Store,
            lastWaiting: Waiting?,
            statusWaitLastWaiting: Waiting?,
        ): Waiting {
            val generatedUuid = generateUuid()
            val generatedWaitingNumber = lastWaiting?.number?.let { it + 1 } ?: 1
            val generatedWaitingTurn = statusWaitLastWaiting?.turn?.let { it + 1 } ?: 0

            return Waiting(
                store = store,
                number = generatedWaitingNumber,
                turn = generatedWaitingTurn,
                adult = waitingDto.adult,
                children = waitingDto.children,
                phoneNumber = waitingDto.phoneNumber,
                status = WAIT,
                messageStatus = SEND_REGISTER,
                id = generatedUuid,
            )
        }

        private fun generateUuid(): UUID {
            // UUID V1 - Sequential UUID
            val uuid = Generators.timeBasedGenerator().generate()
            val uuidArray = uuid.toString().split("-")
            val uuidString = uuidArray[2] + uuidArray[1] + uuidArray[0] + uuidArray[3] + uuidArray[4]

            val sb = StringBuilder(uuidString)
            sb.insert(8, "-")
            sb.insert(13, "-")
            sb.insert(18, "-")
            sb.insert(23, "-")

            return UUID.fromString(sb.toString())
        }
    }

}
