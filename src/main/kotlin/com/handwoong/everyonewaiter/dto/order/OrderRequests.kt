package com.handwoong.everyonewaiter.dto.order

data class OrderRequests(
    val tableNumber: Int,
    val orderMenus: MutableList<OrderMenuRequest>,
)

data class OrderMenuRequest(
    val menuId: Long,
    val name: String,
    val price: Int,
    val qty: Int,
    val option: OrderMenuOption,
    val customOption: String,
)

data class OrderMenuOption(
    val spicy: String,
)

data class DiscountRequest(
    val tableNumber: Int,
    val discountPrice: Int,
)

data class OrderMenuQtyRequest(
    val orderId: Long,
    val orderMenuId: Long,
    val qty: Int,
)
