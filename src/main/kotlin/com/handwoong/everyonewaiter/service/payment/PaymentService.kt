package com.handwoong.everyonewaiter.service.payment

import com.handwoong.everyonewaiter.dto.payment.PaymentExistsResponse
import com.handwoong.everyonewaiter.dto.payment.PaymentRegisterRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentResponse
import java.time.LocalDateTime

interface PaymentService {

    fun existsTablePayment(storeId: Long, tableNumber: Int): PaymentExistsResponse

    fun findOnePayment(storeId: Long, paymentId: Long): PaymentResponse

    fun findAllStorePayment(storeId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<PaymentResponse>

    fun register(storeId: Long, paymentRequests: PaymentRegisterRequest): Long

    fun payment(storeId: Long, paymentId: Long, paymentRequest: PaymentRequest)

    fun cancel(storeId: Long, paymentId: Long, paymentRequest: PaymentRequest)

}
