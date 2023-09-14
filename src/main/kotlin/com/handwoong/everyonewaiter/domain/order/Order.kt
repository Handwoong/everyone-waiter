package com.handwoong.everyonewaiter.domain.order

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.order.OrderMenuStatus.SERVED
import com.handwoong.everyonewaiter.domain.order.OrderStatus.ORDER
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.exception.ErrorCode.ORDER_NOT_AVAILABLE_STATUS
import com.handwoong.everyonewaiter.exception.ErrorCode.TABLE_NUMBER_NOT_VALID
import com.handwoong.everyonewaiter.util.throwFail
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "orders")
class Order(

    var tableNumber: Int,

    var totalPrice: Int = 0,

    var discountPrice: Int = 0,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderMenuList: MutableList<OrderMenu> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addOrderMenu(orderMenu: OrderMenu) {
        orderMenuList.add(orderMenu)
        orderMenu.addOrder(this)
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
        this.tableNumber = tableNumber
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
    }

    fun decreaseTotalPrice(price: Int) {
        totalPrice -= price
    }

    fun discount(price: Int) {
        discountPrice += price
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
            status: OrderStatus = ORDER,
            vararg orderMenu: OrderMenu,
        ): Order {
            val newOrder = Order(
                tableNumber = tableNumber,
                store = store,
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
