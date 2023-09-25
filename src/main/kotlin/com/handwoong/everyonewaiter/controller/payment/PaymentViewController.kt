package com.handwoong.everyonewaiter.controller.payment

import com.handwoong.everyonewaiter.service.payment.PaymentService
import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Controller
class PaymentViewController(
    private val storeService: StoreService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/stores/{storeId}/payment")
    fun paymentList(
        @PathVariable storeId: Long,
        @RequestParam(value = "start", required = false) start: String?,
        @RequestParam(value = "end", required = false) end: String?,
        model: Model,
    ): String {
        val startDate = if (start.isNullOrBlank()) {
            LocalDate.now().atStartOfDay()
        } else {
            val startLocalDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            startLocalDate.atStartOfDay()
        }

        val endDate = if (end.isNullOrBlank()) {
            LocalDateTime.of(LocalDate.now(), LocalTime.MAX).withNano(0)
        } else {
            val endLocalDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            LocalDateTime.of(endLocalDate, LocalTime.MAX).withNano(0)
        }

        val store = storeService.findStore(getAuthenticationUsername(), storeId)
        val findAllStorePayment = paymentService.findAllStorePayment(storeId, startDate, endDate)

        model.addAttribute("store", store)
        model.addAttribute("paymentList", findAllStorePayment)
        return "orders/payment"
    }

}
