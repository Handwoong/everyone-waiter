package com.handwoong.everyonewaiter.controller.waiting

import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.service.waiting.WaitingService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class WaitingViewController(
    private val storeService: StoreService,
    private val waitingService: WaitingService,
) {

    @GetMapping("/members/stores/waiting")
    fun waitingStoreListPage(model: Model): String {
        val storeList = storeService.findStoreList(getAuthenticationUsername())
        model.addAttribute("storeList", storeList)
        return "waiting/index"
    }

    @GetMapping("/waiting/stores/{storeId}")
    fun waitingRegisterForm(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        val waitingCount = waitingService.count(getAuthenticationUsername(), storeId)
        model.addAttribute("countResponse", waitingCount)
        return "waiting/register"
    }

    @GetMapping("/waiting/stores/{storeId}/admin")
    fun waitingAdminPage(
        @PathVariable storeId: Long,
        model: Model,
    ): String {
        val waitingList = waitingService.findStatusWaitWaitingList(getAuthenticationUsername(), storeId)
        model.addAttribute("waitingList", waitingList)
        return "waiting/admin"
    }

}
