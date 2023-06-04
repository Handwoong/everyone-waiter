package com.handwoong.everyonewaiter.controller.store

import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class StoreViewController(
    private val storeService: StoreService,
) {

    @GetMapping("/members/stores")
    fun storeListPage(model: Model): String {
        val storeList = storeService.findStoreList(getAuthenticationUsername())
        model.addAttribute("storeList", storeList)
        return "stores/index"
    }

    @GetMapping("/members/stores/add")
    fun storeRegisterForm(): String {
        return "stores/register"
    }

    @GetMapping("/members/stores/{storeId}/edit")
    fun storeEditForm(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        val store = storeService.findStore(getAuthenticationUsername(), storeId)
        model.addAttribute("store", store)
        return "stores/edit"
    }

}
