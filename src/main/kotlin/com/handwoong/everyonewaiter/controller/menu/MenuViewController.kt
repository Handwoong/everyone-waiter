package com.handwoong.everyonewaiter.controller.menu

import com.handwoong.everyonewaiter.service.category.CategoryService
import com.handwoong.everyonewaiter.service.menu.MenuService
import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class MenuViewController(
    private val storeService: StoreService,
    private val categoryService: CategoryService,
    private val menuService: MenuService,
) {

    @GetMapping("/members/stores/menus")
    fun menuStoreListPage(model: Model): String {
        val storeList = storeService.findStoreList(getAuthenticationUsername())
        model.addAttribute("storeList", storeList)
        return "menus/index"
    }

    @GetMapping("/members/stores/{storeId}/menus")
    fun menuAdminPage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        menuPage(storeId, model)
        return "menus/admin"
    }

    @GetMapping("/menus/stores/{storeId}")
    fun menuCustomerPage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        menuPage(storeId, model)
        return "menus/customer"
    }

    private fun menuPage(storeId: Long, model: Model) {
        val store = storeService.findStore(getAuthenticationUsername(), storeId)
        val categoryList = categoryService.findAllStoreCategory(getAuthenticationUsername(), storeId)
        val menuList = menuService.findAllStoreMenu(storeId)
        model.addAttribute("store", store)
        model.addAttribute("categoryList", categoryList)
        model.addAttribute("menuList", menuList)
    }

}
