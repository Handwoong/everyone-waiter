package com.handwoong.everyonewaiter.service.payment

import com.handwoong.everyonewaiter.domain.payment.Payment
import com.handwoong.everyonewaiter.dto.payment.PaymentExistsResponse
import com.handwoong.everyonewaiter.dto.payment.PaymentRegisterRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentResponse
import com.handwoong.everyonewaiter.repository.order.OrderRepository
import com.handwoong.everyonewaiter.repository.payment.PaymentRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PaymentServiceImpl(
    private val storeRepository: StoreRepository,
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
) : PaymentService {

    @Transactional
    override fun existsTablePayment(storeId: Long, tableNumber: Int): PaymentExistsResponse {
        val store = storeRepository.findByIdOrThrow(storeId)
        val tablePaymentList = paymentRepository.findTablePayment(storeId, tableNumber)

        return if (tablePaymentList.isEmpty()) {
            PaymentExistsResponse.of(0L)
        } else if (tablePaymentList.size == 1) {
            PaymentExistsResponse.of(tablePaymentList[0].id!!)
        } else {
            val createPayment = Payment.createPayment(PaymentRegisterRequest(tableNumber), store)
            val orderList = orderRepository.findStoreTableOrderList(storeId, tableNumber)

            tablePaymentList.forEach { payment ->
                createPayment.reloadPayment(
                    cash = payment.cash,
                    card = payment.card,
                )
                paymentRepository.deleteById(payment.id!!)
            }
            orderList.forEach { createPayment.addOrder(it) }

            paymentRepository.save(createPayment)
            PaymentExistsResponse.of(createPayment.id!!)
        }
    }

    override fun findOnePayment(storeId: Long, paymentId: Long): PaymentResponse {
        storeRepository.findByIdOrThrow(storeId)
        val payment = paymentRepository.findByIdOrThrow(paymentId)
        return PaymentResponse.of(payment)
    }

    override fun findAllStorePayment(
        storeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<PaymentResponse> {
        storeRepository.findByIdOrThrow(storeId)
        val paymentList = paymentRepository.findStorePayment(storeId, startDate, endDate)
        return paymentList.map { payment -> PaymentResponse.of(payment) }
    }

    @Transactional
    override fun register(
        storeId: Long, paymentRequests: PaymentRegisterRequest,
    ): Long {
        val store = storeRepository.findByIdOrThrow(storeId)
        val createPayment = Payment.createPayment(paymentRequests, store)
        val orderList = orderRepository.findStoreTableOrderList(storeId, paymentRequests.tableNumber)

        orderList.forEach { createPayment.addOrder(it) }
        paymentRepository.save(createPayment)
        return createPayment.id!!
    }

    @Transactional
    override fun payment(storeId: Long, paymentId: Long, paymentRequest: PaymentRequest) {
        storeRepository.findByIdOrThrow(storeId)
        val findPayment = paymentRepository.findByIdOrThrow(paymentId)
        findPayment.pay(paymentRequest)
    }

    @Transactional
    override fun cancel(storeId: Long, paymentId: Long, paymentRequest: PaymentRequest) {
        storeRepository.findByIdOrThrow(storeId)
        val findPayment = paymentRepository.findByIdOrThrow(paymentId)
        findPayment.cancel(paymentRequest)
    }

}
