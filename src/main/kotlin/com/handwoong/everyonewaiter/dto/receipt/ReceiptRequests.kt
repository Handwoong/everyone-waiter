package com.handwoong.everyonewaiter.dto.receipt

data class ReceiptRequest(
    val tableNumber: Int,
    val memo: String,
    val receiptMenuList: MutableList<ReceiptMenuRequest>,
)

data class ReceiptMenuRequest(
    val name: String,
    val qty: Int,
    val optionName: String,
    val customOption: String,
)
