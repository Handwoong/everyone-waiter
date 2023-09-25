package com.handwoong.everyonewaiter.domain.payment

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.dto.payment.PaymentRequest
import javax.persistence.*

@Entity
class PaymentApprove(

    @Enumerated(EnumType.STRING)
    val type: PaymentType,

    @Enumerated(EnumType.STRING)
    val approveType: ApproveType,

    val amount: Int,

    val approveNumber: String,

    val approveDate: String,

    val cardMonthInstallments: String,

    val cardNumber: String,

    val cardCategoryName: String,

    val cardPurchaseCompanyName: String,

    val merchantNumber: String,

    val transferDate: String,

    val transactionUniqueNumber: String,

    val vat: String,

    val supplyAmount: String,

    val cashReceiptNumber: String,

    val cashReceiptType: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    var payment: Payment? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_approve_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addPayment(payment: Payment) {
        this.payment = payment
    }

    companion object {
        fun createPaymentApprove(paymentRequest: PaymentRequest): PaymentApprove {
            return PaymentApprove(
                type = paymentRequest.type,
                approveType = paymentRequest.approveType,
                amount = paymentRequest.card + paymentRequest.cash,
                approveNumber = paymentRequest.approveNumber,
                approveDate = paymentRequest.approveDate,
                cardMonthInstallments = paymentRequest.cardMonthInstallments,
                cardNumber = paymentRequest.cardNumber,
                cardCategoryName = paymentRequest.cardCategoryName,
                cardPurchaseCompanyName = paymentRequest.cardPurchaseCompanyName,
                merchantNumber = paymentRequest.merchantNumber,
                transferDate = paymentRequest.transferDate,
                transactionUniqueNumber = paymentRequest.transactionUniqueNumber,
                vat = paymentRequest.vat,
                supplyAmount = paymentRequest.supplyAmount,
                cashReceiptNumber = paymentRequest.cashReceiptNumber,
                cashReceiptType = paymentRequest.cashReceiptType,
            )
        }
    }

}
