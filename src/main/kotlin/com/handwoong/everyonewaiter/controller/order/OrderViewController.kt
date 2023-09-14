package com.handwoong.everyonewaiter.controller.order

import com.handwoong.everyonewaiter.service.category.CategoryService
import com.handwoong.everyonewaiter.service.menu.MenuService
import com.handwoong.everyonewaiter.service.order.OrderService
import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class OrderViewController(
    private val storeService: StoreService,
    private val categoryService: CategoryService,
    private val menuService: MenuService,
    private val orderService: OrderService,
) {

    @GetMapping("/members/stores/orders")
    fun orderStoreList(model: Model): String {
        val storeList = storeService.findStoreList(getAuthenticationUsername())
        model.addAttribute("storeList", storeList)
        return "orders/index"
    }

    @GetMapping("/stores/{storeId}/orders/table")
    fun menuCustomerPage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        val categoryList = categoryService.findAllStoreCategory(getAuthenticationUsername(), storeId)
        val menuList = menuService.findAllStoreMenu(storeId)
        val findAllStoreOrderList = orderService.findAllStoreOrder(storeId)

        pageStoreData(storeId, model)
        model.addAttribute("categoryList", categoryList)
        model.addAttribute("menuList", menuList)
        model.addAttribute("orderList", findAllStoreOrderList)
        return "orders/customer"
    }

    @GetMapping("/stores/{storeId}/orders/table/{tableNumber}")
    fun menuCounterOrderPage(
        @PathVariable storeId: Long,
        @PathVariable tableNumber: Long,
        model: Model,
    ): String {
        val categoryList = categoryService.findAllStoreCategory(getAuthenticationUsername(), storeId)
        val menuList = menuService.findAllStoreMenu(storeId)

        pageStoreData(storeId, model)
        model.addAttribute("categoryList", categoryList)
        model.addAttribute("menuList", menuList)
        return "orders/counterOrder"
    }

    @GetMapping("/stores/{storeId}/orders/serve")
    fun orderServePage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        pageStoreData(storeId, model)
        pageOrderData(storeId, model)
        return "orders/serve"
    }

    @GetMapping("/stores/{storeId}/orders/pos")
    fun orderCounterPosPage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        pageStoreData(storeId, model)
        val findAllStoreOrderList = orderService.findAllStoreOrder(storeId)

        model.addAttribute("orderList", findAllStoreOrderList)
        return "orders/counter"
    }

    private fun pageStoreData(storeId: Long, model: Model) {
        val store = storeService.findStore(getAuthenticationUsername(), storeId)
        model.addAttribute("store", store)
    }

    private fun pageOrderData(storeId: Long, model: Model) {
        val orderList = orderService.findAllStoreOrderStatusOrder(storeId)
        val addOrderList = orderService.findAllStoreOrderStatusAdd(storeId)
        model.addAttribute("orderList", orderList)
        model.addAttribute("addOrderList", addOrderList)
    }

}
