package com.handwoong.everyonewaiter.dto.order

import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderMenu
import com.handwoong.everyonewaiter.domain.order.OrderMenuStatus
import com.handwoong.everyonewaiter.domain.order.OrderStatus
import com.handwoong.everyonewaiter.dto.menu.MenuResponse
import java.time.LocalDateTime

data class OrderResponses(
    val id: Long,
    val tableNumber: Int,
    val totalPrice: Int,
    val discountPrice: Int,
    val orderStatus: OrderStatus,
    val orderMenuList: List<OrderMenuResponse>,
    val createdAt: LocalDateTime,
) {

    companion object {
        fun of(order: Order): OrderResponses {
            val orderMenuList = order.orderMenuList
                .map { orderMenu -> OrderMenuResponse.of(orderMenu) }

            return OrderResponses(
                id = order.id!!,
                tableNumber = order.tableNumber,
                totalPrice = order.totalPrice,
                discountPrice = order.discountPrice,
                orderStatus = order.status,
                orderMenuList = orderMenuList,
                createdAt = order.createdAt,
            )
        }
    }

}

data class OrderMenuResponse(
    val id: Long,
    val orderPrice: Int,
    val orderCount: Int,
    val menuOption: String,
    val customOption: String,
    val menuStatus: OrderMenuStatus,
    val menu: MenuResponse,
) {

    companion object {
        fun of(orderMenu: OrderMenu): OrderMenuResponse {
            return OrderMenuResponse(
                id = orderMenu.id!!,
                orderPrice = orderMenu.orderPrice,
                orderCount = orderMenu.orderCount,
                menuOption = orderMenu.menuOption,
                customOption = orderMenu.customOption,
                menuStatus = orderMenu.menuStatus,
                menu = MenuResponse.of(orderMenu.menu)
            )
        }
    }

}
