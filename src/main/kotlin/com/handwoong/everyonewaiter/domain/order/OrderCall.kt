package com.handwoong.everyonewaiter.domain.order

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.order.OrderCallRequest
import javax.persistence.*

@Entity
class OrderCall(
    val tableNumber: Int,

    val callDetail: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    @Enumerated(EnumType.STRING)
    var status: CallStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_call_id")
    val id: Long? = null,
) : BaseEntity() {

    fun complete() {
        status = CallStatus.COMPLETE
    }

    companion object {
        fun createOrderCall(orderCallRequest: OrderCallRequest, store: Store): OrderCall {
            return OrderCall(
                tableNumber = orderCallRequest.tableNumber,
                callDetail = orderCallRequest.callDetail,
                store = store,
                status = CallStatus.ORDER,
            )
        }
    }

}
