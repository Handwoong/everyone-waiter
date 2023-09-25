package com.handwoong.everyonewaiter.domain.waiting

import com.fasterxml.uuid.Generators
import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.domain.waiting.WaitingMessageStatus.*
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus.*
import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.util.throwFail
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
        if (status == WAIT) {
            throwFail(WAITING_NOT_AVAILABLE_STATUS)
        }

        this.status = status
        this.turn = -1
    }

    fun changeMessageStatus(status: WaitingMessageStatus) {
        if (status == SEND_NOT) {
            throwFail(WAITING_NOT_AVAILABLE_STATUS)
        }

        if (this.messageStatus != status) {
            this.messageStatus = status
        }
    }

    companion object {
        fun createWaiting(
            waitingDto: WaitingRegisterRequest,
            store: Store,
            number: Int = 1,
            turn: Int = 0,
        ): Waiting {
            val generatedUuid = generateUuid()

            return Waiting(
                store = store,
                number = number,
                turn = turn,
                adult = waitingDto.adult,
                children = waitingDto.children,
                phoneNumber = waitingDto.phoneNumber,
                status = WAIT,
                messageStatus = SEND_NOT,
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
