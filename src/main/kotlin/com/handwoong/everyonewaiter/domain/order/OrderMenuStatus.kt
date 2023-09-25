package com.handwoong.everyonewaiter.domain.order

enum class OrderMenuStatus(
    val description: String,
) {
    READY("준비중"),
    SERVED("서빙 완료"),
}
