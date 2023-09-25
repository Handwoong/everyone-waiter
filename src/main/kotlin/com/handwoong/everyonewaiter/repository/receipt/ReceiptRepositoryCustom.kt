package com.handwoong.everyonewaiter.repository.receipt

import com.handwoong.everyonewaiter.domain.receipt.Receipt

interface ReceiptRepositoryCustom {

    fun findAllStorePrint(storeId: Long): List<Receipt>

}
