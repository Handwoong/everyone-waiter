package com.handwoong.everyonewaiter.repository.order

import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderStatus

interface OrderRepositoryCustom {

    fun existsOrder(
        storeId: Long,
        tableNumber: Int,
    ): Boolean

    fun findAllStoreOrder(
        storeId: Long,
        orderStatus: OrderStatus?,
    ): List<Order>

    fun findAllStoreOrderNotServe(
        storeId: Long,
    ): List<Order>

    fun findStoreTableOrderList(
        storeId: Long,
        tableNumber: Int? = null,
    ): List<Order>

}
