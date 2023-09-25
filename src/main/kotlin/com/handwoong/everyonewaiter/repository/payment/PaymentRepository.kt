package com.handwoong.everyonewaiter.repository.payment

import com.handwoong.everyonewaiter.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long>, PaymentRepositoryCustom
