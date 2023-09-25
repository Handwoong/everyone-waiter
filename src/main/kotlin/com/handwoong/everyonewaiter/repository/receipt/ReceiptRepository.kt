package com.handwoong.everyonewaiter.repository.receipt

import com.handwoong.everyonewaiter.domain.receipt.Receipt
import org.springframework.data.jpa.repository.JpaRepository

interface ReceiptRepository : JpaRepository<Receipt, Long>, ReceiptRepositoryCustom
