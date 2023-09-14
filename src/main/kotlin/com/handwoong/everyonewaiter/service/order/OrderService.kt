package com.handwoong.everyonewaiter.service.order

import com.handwoong.everyonewaiter.dto.order.DiscountRequest
import com.handwoong.everyonewaiter.dto.order.OrderMenuQtyRequest
import com.handwoong.everyonewaiter.dto.order.OrderRequests
import com.handwoong.everyonewaiter.dto.order.OrderResponses

interface OrderService {

    fun register(storeId: Long, orderRequest: OrderRequests)

    fun changeStatusOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long)

    fun changeOrderTableNumber(storeId: Long, beforeTableNumber: Int, afterTableNumber: Int)

    fun changeOrderMenuQty(storeId: Long, orderMenuQtyRequest: OrderMenuQtyRequest)

    fun discount(storeId: Long, discountRequest: DiscountRequest)

    fun findAllStoreOrderStatusOrder(storeId: Long): List<OrderResponses>

    fun findAllStoreOrderStatusAdd(storeId: Long): List<OrderResponses>

    fun findAllStoreOrder(storeId: Long): List<OrderResponses>

    fun deleteOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long)

}
