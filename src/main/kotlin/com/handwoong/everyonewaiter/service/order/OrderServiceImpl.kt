package com.handwoong.everyonewaiter.service.order

import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderMenu
import com.handwoong.everyonewaiter.domain.order.OrderStatus
import com.handwoong.everyonewaiter.dto.order.DiscountRequest
import com.handwoong.everyonewaiter.dto.order.OrderMenuQtyRequest
import com.handwoong.everyonewaiter.dto.order.OrderRequests
import com.handwoong.everyonewaiter.dto.order.OrderResponses
import com.handwoong.everyonewaiter.exception.ErrorCode
import com.handwoong.everyonewaiter.repository.menu.MenuRepository
import com.handwoong.everyonewaiter.repository.order.OrderRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.findByIdOrThrow
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderServiceImpl(
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val orderRepository: OrderRepository,
) : OrderService {

    @Transactional
    override fun register(storeId: Long, orderRequest: OrderRequests) {
        val store = storeRepository.findByIdOrThrow(storeId)
        val isExistsOrder = orderRepository.existsOrder(storeId, orderRequest.tableNumber)

        val orderMenuList = orderRequest.orderMenus.map { menu ->
            val findMenu = menuRepository.findByIdOrThrow(menu.menuId)
            OrderMenu.createOrderMenu(findMenu, menu)
        }
        val createOrder = Order.createOrder(
            tableNumber = orderRequest.tableNumber,
            store = store,
            orderMenu = orderMenuList.toTypedArray()
        )

        if (isExistsOrder) {
            createOrder.changeOrderStatus(OrderStatus.ADD)
        }

        orderRepository.save(createOrder)
    }

    @Transactional
    override fun changeStatusOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val order = orderRepository.findByIdOrThrow(orderId)
        order.servedMenu(orderMenuId)
    }

    @Transactional
    override fun changeOrderTableNumber(storeId: Long, beforeTableNumber: Int, afterTableNumber: Int) {
        storeRepository.findByIdOrThrow(storeId)
        val orderList = orderRepository.findStoreTableOrderList(storeId, beforeTableNumber)
        orderList.forEach { order -> order.changeTableNumber(afterTableNumber) }
    }

    @Transactional
    override fun changeOrderMenuQty(storeId: Long, orderMenuQtyRequest: OrderMenuQtyRequest) {
        storeRepository.findByIdOrThrow(storeId)
        val findOrder = orderRepository.findByIdOrThrow(orderMenuQtyRequest.orderId)
        findOrder.changeOrderMenuQty(orderMenuQtyRequest.orderMenuId, orderMenuQtyRequest.qty)
    }

    @Transactional
    override fun discount(storeId: Long, discountRequest: DiscountRequest) {
        storeRepository.findByIdOrThrow(storeId)
        val orderList = orderRepository.findStoreTableOrderList(storeId, discountRequest.tableNumber)

        if (orderList.isEmpty()) {
            throwFail(ErrorCode.METHOD_ARGUMENT_NOT_VALID)
        }
        orderList[0].discount(discountRequest.discountPrice)
    }

    override fun findAllStoreOrderStatusOrder(storeId: Long): List<OrderResponses> {
        val findAllStoreOrder = orderRepository.findAllStoreOrder(storeId, OrderStatus.ORDER)
        return findAllStoreOrder.map { order -> OrderResponses.of(order) }
    }

    override fun findAllStoreOrderStatusAdd(storeId: Long): List<OrderResponses> {
        val findAllStoreAddOrder = orderRepository.findAllStoreOrder(storeId, OrderStatus.ADD)
        return findAllStoreAddOrder.map { order -> OrderResponses.of(order) }
    }

    override fun findAllStoreOrder(storeId: Long): List<OrderResponses> {
        val findStoreNotPaymentOrder = orderRepository.findStoreTableOrderList(storeId)
        return findStoreNotPaymentOrder.map { order -> OrderResponses.of(order) }
    }

    @Transactional
    override fun deleteOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val findOrder = orderRepository.findByIdOrThrow(orderId)
        findOrder.deleteOrderMenu(orderMenuId)

        if (findOrder.orderMenuList.isEmpty()) {
            orderRepository.deleteById(findOrder.id!!)
        }
    }

}
