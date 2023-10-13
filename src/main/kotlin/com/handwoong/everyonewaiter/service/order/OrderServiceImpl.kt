package com.handwoong.everyonewaiter.service.order

import com.handwoong.everyonewaiter.domain.order.Order
import com.handwoong.everyonewaiter.domain.order.OrderCall
import com.handwoong.everyonewaiter.domain.order.OrderMenu
import com.handwoong.everyonewaiter.domain.order.OrderStatus
import com.handwoong.everyonewaiter.dto.order.*
import com.handwoong.everyonewaiter.exception.ErrorCode
import com.handwoong.everyonewaiter.repository.menu.MenuRepository
import com.handwoong.everyonewaiter.repository.order.OrderRepository
import com.handwoong.everyonewaiter.repository.ordercall.OrderCallRepository
import com.handwoong.everyonewaiter.repository.payment.PaymentRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.ExcludeLog
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
    private val orderCallRepository: OrderCallRepository,
    private val paymentRepository: PaymentRepository,
) : OrderService {

    @Transactional
    override fun register(storeId: Long, orderRequest: OrderRequests) {
        val store = storeRepository.findByIdOrThrow(storeId)
        val isExistsOrder = orderRepository.existsOrder(storeId, orderRequest.tableNumber)
        val findTablePayment = paymentRepository.findTablePayment(storeId, orderRequest.tableNumber)

        val orderMenuList = orderRequest.orderMenus.map { menu ->
            val findMenu = menuRepository.findByIdOrThrow(menu.menuId)
            OrderMenu.createOrderMenu(findMenu, menu)
        }
        val createOrder = Order.createOrder(
            tableNumber = orderRequest.tableNumber,
            store = store,
            memo = orderRequest.memo,
            orderMenu = orderMenuList.toTypedArray()
        )

        if (isExistsOrder) {
            createOrder.changeOrderStatus(OrderStatus.ADD)
        }

        if (findTablePayment.isNotEmpty()) {
            findTablePayment[0].addOrder(createOrder)
        }

        orderRepository.save(createOrder)
    }

    @Transactional
    override fun callRegister(storeId: Long, orderCallRequest: OrderCallRequest) {
        val store = storeRepository.findByIdOrThrow(storeId)
        val createOrderCall = OrderCall.createOrderCall(orderCallRequest, store)
        orderCallRepository.save(createOrderCall)
    }

    @Transactional
    override fun changeStatusAllOrderMenu(storeId: Long, orderId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val order = orderRepository.findByIdOrThrow(orderId)
        order.changeOrderStatus(OrderStatus.SERVED)
    }

    @Transactional
    override fun changeStatusOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val order = orderRepository.findByIdOrThrow(orderId)
        order.servedMenu(orderMenuId)
    }

    @Transactional
    override fun changeStatusOrderCall(storeId: Long, orderCallId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val orderCall = orderCallRepository.findByIdOrThrow(orderCallId)
        orderCall.complete()
    }

    @Transactional
    override fun changeOrderTableNumber(storeId: Long, beforeTableNumber: Int, afterTableNumber: Int) {
        storeRepository.findByIdOrThrow(storeId)
        val beforeTableOrderList = orderRepository.findStoreTableOrderList(storeId, beforeTableNumber)
        val afterTableOrderList = orderRepository.findStoreTableOrderList(storeId, afterTableNumber)
        beforeTableOrderList.forEach { order -> order.changeTableNumber(afterTableNumber) }
        afterTableOrderList.forEach { order -> order.changeTableNumber(afterTableNumber) }

        if (beforeTableOrderList.isNotEmpty()) {
            beforeTableOrderList[0].payment?.let { payment -> paymentRepository.delete(payment) }
        }

        if (afterTableOrderList.isNotEmpty()) {
            afterTableOrderList[0].payment?.let { payment -> paymentRepository.delete(payment) }
        }
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

    @ExcludeLog
    override fun findAllStoreStatusNotServe(storeId: Long): List<OrderResponses> {
        val findAllStoreAddOrder = orderRepository.findAllStoreOrderNotServe(storeId)
        return findAllStoreAddOrder.map { order -> OrderResponses.of(order) }
    }

    @ExcludeLog
    override fun findAllStoreOrder(storeId: Long): List<OrderResponses> {
        val findStoreNotPaymentOrder = orderRepository.findStoreTableOrderList(storeId)
        return findStoreNotPaymentOrder.map { order -> OrderResponses.of(order) }
    }

    @ExcludeLog
    override fun findAllStoreOrderCall(storeId: Long): List<OrderCallResponse> {
        val findAllStoreOrderCall = orderCallRepository.findAllStoreOrderCall(storeId)
        return findAllStoreOrderCall.map { orderCall -> OrderCallResponse.of(orderCall) }
    }

    @Transactional
    override fun deleteOrderMenu(storeId: Long, orderId: Long, orderMenuId: Long) {
        storeRepository.findByIdOrThrow(storeId)
        val findOrder = orderRepository.findByIdOrThrow(orderId)
        findOrder.deleteOrderMenu(orderMenuId)

        if (findOrder.orderMenuList.isEmpty()) {
            findOrder.payment?.let { payment ->
                payment.cancelDiscount(findOrder.discountPrice)
                if (payment.orderList.size == 1) {
                    payment.disconnectOrder(findOrder)
                    paymentRepository.delete(payment)
                }
            }
            orderRepository.delete(findOrder)
        }
    }

}
