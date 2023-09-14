package com.handwoong.everyonewaiter.repository.order

import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderStatus
import com.handwoong.everyonewaiter.domain.order.QOrder.order
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : OrderRepositoryCustom {

    override fun existsOrder(
        storeId: Long,
        tableNumber: Int
    ): Boolean {
        val findOrder = queryFactory.select(order)
            .from(order)
            .where(
                order.store.id.eq(storeId),
                order.tableNumber.eq(tableNumber),
                order.status.eq(OrderStatus.ORDER).or(order.status.eq(OrderStatus.SERVED)),
            )
            .fetchFirst()
        return findOrder != null
    }

    override fun findAllStoreOrder(
        storeId: Long,
        orderStatus: OrderStatus?,
    ): List<Order> {
        return queryFactory.select(order)
            .from(order)
            .where(
                order.store.id.eq(storeId),
                orderStatus?.let { order.status.eq(orderStatus) }
            )
            .orderBy(order.createdAt.asc())
            .fetch()
    }

    override fun findStoreTableOrderList(
        storeId: Long,
        tableNumber: Int?,
    ): List<Order> {
        return queryFactory.select(order)
            .from(order)
            .where(
                order.store.id.eq(storeId),
                tableNumber?.let { order.tableNumber.eq(tableNumber) },
            )
            .fetch()
    }

}
