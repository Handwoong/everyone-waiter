package com.handwoong.everyonewaiter.domain.payment

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderStatus
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.payment.PaymentRegisterRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentRequest
import com.handwoong.everyonewaiter.exception.ErrorCode
import com.handwoong.everyonewaiter.util.throwFail
import javax.persistence.*

@Entity
class Payment(

    var tableNumber: Int,

    var cash: Int = 0,

    var card: Int = 0,

    var total: Int = 0,

    var discount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    @OneToMany(mappedBy = "payment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val approve: MutableList<PaymentApprove> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var status: PaymentStatus,

    @OneToMany(mappedBy = "payment")
    val orderList: MutableList<Order> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addOrder(order: Order) {
        order.addPayment(this)
        orderList.add(order)
        total += order.totalPrice
        discount += order.discountPrice
    }

    fun increaseTotalPrice(price: Int) {
        total += price
    }

    fun decreaseTotalPrice(price: Int) {
        total -= price
    }

    fun discount(price: Int) {
        discount += price
    }

    fun cancelDiscount(price: Int) {
        discount -= price
    }

    fun pay(paymentRequest: PaymentRequest) {
        if (tableNumber != paymentRequest.tableNumber) {
            return
        }

        cash += paymentRequest.cash
        card += paymentRequest.card

        if (total - discount < cash + card) {
            throwFail(ErrorCode.NOT_VALID_PAYMENT_PRICE)
        }

        addPaymentApprove(paymentRequest)

        if (total - discount == cash + card) {
            status = if (cash == 0) {
                PaymentStatus.CARD
            } else if (card == 0) {
                PaymentStatus.CASH
            } else {
                PaymentStatus.ALL
            }
            orderList.forEach { order ->
                order.changeOrderStatus(OrderStatus.COMPLETE_PAYMENT)
            }
        }
    }

    fun cancel(paymentRequest: PaymentRequest) {
        cash -= paymentRequest.cash
        card -= paymentRequest.card

        addPaymentApprove(paymentRequest)
        status = PaymentStatus.CANCEL
    }

    fun reloadPayment(
        cash: Int,
        card: Int,
        discount: Int,
    ) {
        this.cash += cash
        this.card += card
        this.discount += discount
    }

    private fun addPaymentApprove(paymentRequest: PaymentRequest) {
        if (paymentRequest.approveType == ApproveType.CARD) {
            if (paymentRequest.approveNumber.isEmpty() || paymentRequest.approveDate.isEmpty()) {
                throwFail(ErrorCode.NOT_VALID_PAYMENT_PRICE)
            }
        }

        val createPaymentApprove = PaymentApprove.createPaymentApprove(paymentRequest)
        this.approve.add(createPaymentApprove)
        createPaymentApprove.addPayment(this)
    }

    companion object {
        fun createPayment(
            paymentRequests: PaymentRegisterRequest,
            store: Store,
        ): Payment {
            return Payment(
                tableNumber = paymentRequests.tableNumber,
                status = PaymentStatus.PROCEEDING,
                store = store,
            )
        }
    }

}
