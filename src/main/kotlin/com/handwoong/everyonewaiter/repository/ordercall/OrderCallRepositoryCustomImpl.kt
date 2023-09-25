package com.handwoong.everyonewaiter.repository.ordercall

import com.handwoong.everyonewaiter.domain.order.CallStatus
import com.handwoong.everyonewaiter.domain.order.OrderCall
import com.handwoong.everyonewaiter.domain.order.QOrderCall.orderCall
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class OrderCallRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : OrderCallRepositoryCustom {

    override fun findAllStoreOrderCall(storeId: Long): List<OrderCall> {
        return queryFactory.select(orderCall)
            .from(orderCall)
            .where(
                orderCall.store.id.eq(storeId),
                orderCall.status.eq(CallStatus.ORDER),
            )
            .fetch()
    }

}
