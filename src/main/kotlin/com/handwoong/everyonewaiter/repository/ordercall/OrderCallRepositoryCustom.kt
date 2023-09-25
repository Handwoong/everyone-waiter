package com.handwoong.everyonewaiter.repository.ordercall

import com.handwoong.everyonewaiter.domain.order.OrderCall

interface OrderCallRepositoryCustom {

    fun findAllStoreOrderCall(storeId: Long): List<OrderCall>

}
