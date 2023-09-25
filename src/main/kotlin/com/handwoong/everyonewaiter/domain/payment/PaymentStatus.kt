package com.handwoong.everyonewaiter.domain.payment

enum class PaymentStatus(
    val description: String,
) {
    PROCEEDING("진행 중"),
    CASH("현금"),
    CARD("카드"),
    ALL("현금 및 카드"),
    CANCEL("취소"),
}
