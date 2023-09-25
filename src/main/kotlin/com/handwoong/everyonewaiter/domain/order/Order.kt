package com.handwoong.everyonewaiter.domain.order

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.order.OrderMenuStatus.SERVED
import com.handwoong.everyonewaiter.domain.order.OrderStatus.ORDER
import com.handwoong.everyonewaiter.domain.order.OrderStatus.PAYING
import com.handwoong.everyonewaiter.domain.payment.Payment
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.util.throwFail
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "orders")
class Order(

    var tableNumber: Int,

    var totalPrice: Int = 0,

    var discountPrice: Int = 0,

    val memo: String = "",

    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderMenuList: MutableList<OrderMenu> = mutableListOf(),

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payment_id")
    var payment: Payment? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addOrderMenu(orderMenu: OrderMenu) {
        orderMenuList.add(orderMenu)
        orderMenu.addOrder(this)
    }

    fun addPayment(payment: Payment) {
        this.payment = payment

        if (status == OrderStatus.SERVED) {
            this.changeOrderStatus(PAYING)
        }
    }

    fun servedMenu(orderMenuId: Long) {
        var isNotServedStatus = false

        orderMenuList.forEach { orderMenu ->
            if (orderMenu.id == orderMenuId) {
                orderMenu.changeStatus(SERVED)
            }

            if (orderMenu.menuStatus != SERVED) {
                isNotServedStatus = true
            }
        }

        if (!isNotServedStatus) {
            this.changeOrderStatus(OrderStatus.SERVED)
        }
    }

    fun changeOrderStatus(orderStatus: OrderStatus) {
        if (orderStatus == ORDER) {
            throwFail(ORDER_NOT_AVAILABLE_STATUS)
        }
        status = orderStatus
    }

    fun changeTableNumber(tableNumber: Int) {
        if (tableNumber <= 0 || tableNumber > 15) {
            throwFail(TABLE_NUMBER_NOT_VALID)
        }

        if (this.tableNumber != tableNumber) {
            this.tableNumber = tableNumber
        }

        payment.let { payment ->
            if (payment?.approve?.size != 0) {
                throwFail(ALREADY_PROCEEDING_PAYMENT)
            }
        }
    }

    fun changeOrderMenuQty(orderMenuId: Long, qty: Int) {
        orderMenuList.forEach { orderMenu ->
            if (orderMenu.id == orderMenuId) {
                orderMenu.changeQty(qty)
            }
        }
    }

    fun increaseTotalPrice(price: Int) {
        totalPrice += price
        payment.let { payment -> payment?.increaseTotalPrice(price) }
    }

    fun decreaseTotalPrice(price: Int) {
        totalPrice -= price
        payment.let { payment -> payment?.decreaseTotalPrice(price) }
    }

    fun discount(price: Int) {
        discountPrice += price
        payment.let { payment -> payment?.discount(price) }
    }

    fun deleteOrderMenu(orderMenuId: Long) {
        val iterator = orderMenuList.iterator()
        while (iterator.hasNext()) {
            val orderMenu = iterator.next()
            if (orderMenu.id == orderMenuId) {
                orderMenu.disconnectOrder()
                iterator.remove()
                break
            }
        }
    }

    fun removeOrderMenu(orderMenu: OrderMenu) {
        totalPrice -= orderMenu.orderPrice
    }

    companion object {
        fun createOrder(
            tableNumber: Int,
            store: Store,
            memo: String,
            status: OrderStatus = ORDER,
            vararg orderMenu: OrderMenu,
        ): Order {
            val newOrder = Order(
                tableNumber = tableNumber,
                store = store,
                memo = memo,
                status = status,
            )
            orderMenu.forEach {
                newOrder.addOrderMenu(it)
                newOrder.totalPrice += it.orderPrice
            }
            return newOrder
        }
    }

}
