package com.handwoong.everyonewaiter.service.receipt

import com.handwoong.everyonewaiter.domain.receipt.Receipt
import com.handwoong.everyonewaiter.dto.receipt.ReceiptRequest
import com.handwoong.everyonewaiter.dto.receipt.ReceiptResponse
import com.handwoong.everyonewaiter.repository.receipt.ReceiptRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReceiptServiceImpl(
    private val storeRepository: StoreRepository,
    private val receiptRepository: ReceiptRepository,
) : ReceiptService {

    @Transactional
    override fun register(storeId: Long, receiptRequest: ReceiptRequest) {
        val store = storeRepository.findByIdOrThrow(storeId)
        val createReceipt = Receipt.createReceipt(store, receiptRequest)
        receiptRepository.save(createReceipt)
    }

    @Transactional
    override fun changeReceiptStatus(receiptId: Long) {
        val findReceipt = receiptRepository.findByIdOrThrow(receiptId)
        findReceipt.print()
    }

    override fun findStoreReceiptList(storeId: Long): List<ReceiptResponse> {
        val receiptList = receiptRepository.findAllStorePrint(storeId)
        return receiptList.map { receipt -> ReceiptResponse.of(receipt) }
    }

}
