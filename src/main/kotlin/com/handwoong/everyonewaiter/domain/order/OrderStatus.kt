package com.handwoong.everyonewaiter.domain.order

enum class OrderStatus(
    val description: String,
) {
    ORDER("주문"),
    ADD("추가"),
    SERVED("서빙 완료"),
    PAYING("결제 진행 중"),
    COMPLETE_PAYMENT("결제 완료"),
}
