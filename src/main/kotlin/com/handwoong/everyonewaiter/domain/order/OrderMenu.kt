package com.handwoong.everyonewaiter.domain.order

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.domain.order.OrderMenuStatus.READY
import com.handwoong.everyonewaiter.dto.order.OrderMenuRequest
import com.handwoong.everyonewaiter.exception.ErrorCode.IS_SOLD_OUT_MENU
import com.handwoong.everyonewaiter.util.throwFail
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class OrderMenu(

    var orderPrice: Int,

    var orderCount: Int,

    val menuOption: String,

    val customOption: String,

    @Enumerated(EnumType.STRING)
    var menuStatus: OrderMenuStatus,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    val menu: Menu,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addOrder(order: Order) {
        this.order = order
    }

    fun disconnectOrder() {
        this.order?.removeOrderMenu(this)
    }

    fun changeStatus(status: OrderMenuStatus) {
        menuStatus = status
    }

    fun changeQty(qty: Int) {
        val menuPrice = orderPrice / orderCount
        orderCount += qty

        if (qty > 0) {
            orderPrice += menuPrice
            order?.increaseTotalPrice(menuPrice)
        } else {
            orderPrice -= menuPrice
            order?.decreaseTotalPrice(menuPrice)
        }

        if (orderCount <= 0) {
            this.disconnectOrder()
        }
    }

    companion object {
        fun createOrderMenu(
            menu: Menu,
            requestMenu: OrderMenuRequest,
            menuStatus: OrderMenuStatus = READY,
        ): OrderMenu {

            if (menu.option.isSoldOut) {
                throwFail(IS_SOLD_OUT_MENU)
            }

            return OrderMenu(
                menu = menu,
                orderCount = requestMenu.qty,
                orderPrice = requestMenu.price * requestMenu.qty,
                menuOption = requestMenu.option.spicy,
                customOption = requestMenu.customOption,
                menuStatus = menuStatus,
            )
        }
    }

}
