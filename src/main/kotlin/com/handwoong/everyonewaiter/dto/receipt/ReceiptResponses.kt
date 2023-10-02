package com.handwoong.everyonewaiter.dto.receipt

import com.handwoong.everyonewaiter.domain.receipt.Receipt
import com.handwoong.everyonewaiter.domain.receipt.ReceiptMenu

data class ReceiptResponse(
    val id: Long,
    val printIdx: Long,
    val tableNumber: Int,
    val memo: String,
    val receiptMenuList: List<ReceiptMenuResponse>,
) {

    companion object {
        fun of(receipt: Receipt, printIdx: Long): ReceiptResponse {
            val receiptMenuResponseList = receipt.menuList.map { printMenu -> ReceiptMenuResponse.of(printMenu) }
            return ReceiptResponse(
                id = receipt.id!!,
                tableNumber = receipt.tableNumber,
                memo = receipt.memo,
                receiptMenuList = receiptMenuResponseList,
                printIdx = printIdx,
            )
        }
    }

}

data class ReceiptMenuResponse(
    val name: String,
    val qty: Int,
    val optionName: String,
    val customOption: String,
) {

    companion object {
        fun of(receiptMenu: ReceiptMenu): ReceiptMenuResponse {
            return ReceiptMenuResponse(
                name = receiptMenu.name,
                qty = receiptMenu.qty,
                optionName = receiptMenu.optionName,
                customOption = receiptMenu.customOption,
            )
        }
    }

}
