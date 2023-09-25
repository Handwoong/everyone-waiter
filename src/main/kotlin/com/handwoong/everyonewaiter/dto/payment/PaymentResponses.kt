package com.handwoong.everyonewaiter.dto.payment

import com.handwoong.everyonewaiter.domain.payment.*
import com.handwoong.everyonewaiter.dto.order.OrderResponses
import java.time.LocalDateTime

data class PaymentResponse(
    val id: Long,
    val tableNumber: Int,
    val cash: Int,
    val card: Int,
    val status: PaymentStatus,
    val approve: List<PaymentApproveResponse>,
    val orderList: List<OrderResponses>,
    val total: Int,
    val discount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    companion object {
        fun of(
            payment: Payment,
        ): PaymentResponse {
            val approveList = payment.approve.map { paymentApprove ->
                PaymentApproveResponse.of(paymentApprove)
            }
            val orderList = payment.orderList.map { order -> OrderResponses.of(order) }

            return PaymentResponse(
                id = payment.id!!,
                tableNumber = payment.tableNumber,
                cash = payment.cash,
                card = payment.card,
                status = payment.status,
                approve = approveList,
                orderList = orderList,
                total = payment.total,
                discount = payment.discount,
                createdAt = payment.createdAt,
                updatedAt = payment.updatedAt,
            )
        }
    }

}

data class PaymentExistsResponse(
    val id: Long,
) {

    companion object {
        fun of(
            id: Long,
        ): PaymentExistsResponse {
            return PaymentExistsResponse(
                id = id,
            )
        }
    }

}

data class PaymentApproveResponse(
    val id: Long,
    val type: PaymentType,
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
    val createdAt: LocalDateTime,
) {

    companion object {
        fun of(
            paymentApprove: PaymentApprove,
        ): PaymentApproveResponse {
            return PaymentApproveResponse(
                id = paymentApprove.id!!,
                type = paymentApprove.type,
                approveType = paymentApprove.approveType,
                amount = paymentApprove.amount,
                approveNumber = paymentApprove.approveNumber,
                approveDate = paymentApprove.approveDate,
                cardMonthInstallments = paymentApprove.cardMonthInstallments,
                cardNumber = paymentApprove.cardNumber,
                cardCategoryName = paymentApprove.cardCategoryName,
                cardPurchaseCompanyName = paymentApprove.cardPurchaseCompanyName,
                merchantNumber = paymentApprove.merchantNumber,
                transferDate = paymentApprove.transferDate,
                transactionUniqueNumber = paymentApprove.transactionUniqueNumber,
                vat = paymentApprove.vat,
                supplyAmount = paymentApprove.supplyAmount,
                cashReceiptNumber = paymentApprove.cashReceiptNumber,
                cashReceiptType = paymentApprove.cashReceiptType,
                createdAt = paymentApprove.createdAt,
            )
        }
    }

}
