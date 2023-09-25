package com.handwoong.everyonewaiter.dto.socket

import com.handwoong.everyonewaiter.dto.order.OrderMenuOption

data class OrderSocketMessageDto(
    val storeId: String = "",
    val message: MutableList<OrderMessage> = mutableListOf(),
)

data class OrderMessage(
    val id: Long,
    val menuId: Long,
    val name: String,
    val image: String,
    val price: Int,
    val qty: Int,
    val isPrintKitchen: Boolean,
    val option: OrderMenuOption,
    val customOption: String,
)
