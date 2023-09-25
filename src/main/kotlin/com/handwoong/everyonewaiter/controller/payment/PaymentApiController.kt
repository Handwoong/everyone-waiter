package com.handwoong.everyonewaiter.controller.payment

import com.handwoong.everyonewaiter.dto.payment.PaymentExistsResponse
import com.handwoong.everyonewaiter.dto.payment.PaymentRegisterRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentRequest
import com.handwoong.everyonewaiter.dto.payment.PaymentResponse
import com.handwoong.everyonewaiter.service.payment.PaymentService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PaymentApiController(
    private val paymentService: PaymentService,
) {

    @GetMapping("/stores/{storeId}/table/{tableNumber}/payment")
    fun existsTablePayment(
        @PathVariable storeId: Long,
        @PathVariable tableNumber: Int,
    ): ResponseEntity<PaymentExistsResponse> {
        val paymentExistsResponse = paymentService.existsTablePayment(storeId, tableNumber)
        return ResponseEntity(paymentExistsResponse, OK)
    }

    @GetMapping("/stores/{storeId}/payment/{paymentId}")
    fun findPayment(
        @PathVariable storeId: Long,
        @PathVariable paymentId: Long,
    ): ResponseEntity<PaymentResponse> {
        val paymentResponse = paymentService.findOnePayment(storeId, paymentId)
        return ResponseEntity(paymentResponse, OK)
    }

    @PostMapping("/stores/{storeId}/payment")
    fun registerPayment(
        @PathVariable storeId: Long,
        @RequestBody paymentRequest: PaymentRegisterRequest,
    ): ResponseEntity<Long> {
        val paymentId = paymentService.register(storeId, paymentRequest)
        return ResponseEntity(paymentId, CREATED)
    }

    @PutMapping("/stores/{storeId}/payment/{paymentId}")
    fun payment(
        @PathVariable storeId: Long,
        @PathVariable paymentId: Long,
        @RequestBody paymentRequest: PaymentRequest,
    ): ResponseEntity<Unit> {
        paymentService.payment(storeId, paymentId, paymentRequest)
        return ResponseEntity(OK)
    }

    @PutMapping("/stores/{storeId}/payment/{paymentId}/cancel")
    fun cancelPayment(
        @PathVariable storeId: Long,
        @PathVariable paymentId: Long,
        @RequestBody paymentRequest: PaymentRequest,
    ): ResponseEntity<Unit> {
        paymentService.cancel(storeId, paymentId, paymentRequest)
        return ResponseEntity(OK)
    }

}
