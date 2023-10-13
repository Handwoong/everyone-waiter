package com.handwoong.everyonewaiter.service.order

import com.handwoong.everyonewaiter.dto.order.*

interface OrderService {

    fun register(storeId: Long, orderRequest: OrderRequests)

    fun callRegister(storeId: Long, orderCallRequest: OrderCallRequest)

    fun changeStatusAllOrderMenu(storeId: Long, orderId: Long)
    
    fun changeStatusOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long)

    fun changeStatusOrderCall(storeId: Long, orderCallId: Long)

    fun changeOrderTableNumber(storeId: Long, beforeTableNumber: Int, afterTableNumber: Int)

    fun changeOrderMenuQty(storeId: Long, orderMenuQtyRequest: OrderMenuQtyRequest)

    fun discount(storeId: Long, discountRequest: DiscountRequest)

    fun findAllStoreStatusNotServe(storeId: Long): List<OrderResponses>

    fun findAllStoreOrder(storeId: Long): List<OrderResponses>

    fun findAllStoreOrderCall(storeId: Long): List<OrderCallResponse>

    fun deleteOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long)

}
