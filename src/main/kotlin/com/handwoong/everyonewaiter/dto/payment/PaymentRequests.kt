package com.handwoong.everyonewaiter.dto.payment

import com.handwoong.everyonewaiter.domain.payment.ApproveType
import com.handwoong.everyonewaiter.domain.payment.PaymentType

data class PaymentRegisterRequest(
    val tableNumber: Int,
)

data class PaymentRequest(
    val tableNumber: Int,
    val cash: Int,
    val card: Int,
    val type: PaymentType,
    val approveType: ApproveType,
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
)
