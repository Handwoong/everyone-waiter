package com.handwoong.everyonewaiter.repository.payment

import com.handwoong.everyonewaiter.domain.payment.Payment
import java.time.LocalDateTime

interface PaymentRepositoryCustom {

    fun findTablePayment(
        storeId: Long,
        tableNumber: Int,
    ): List<Payment>

    fun findStorePayment(
        storeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Payment>

}
