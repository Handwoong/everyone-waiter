package com.handwoong.everyonewaiter.service.receipt

import com.handwoong.everyonewaiter.dto.receipt.ReceiptRequest
import com.handwoong.everyonewaiter.dto.receipt.ReceiptResponse

interface ReceiptService {

    fun register(storeId: Long, receiptRequest: ReceiptRequest)

    fun changeReceiptStatus(receiptId: Long)

    fun findStoreReceiptList(storeId: Long): List<ReceiptResponse>

}
