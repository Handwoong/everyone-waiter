package com.handwoong.everyonewaiter.controller.waiting

import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.service.waiting.WaitingService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
class WaitingApiController(
    private val storeService: StoreService,
    private val waitingService: WaitingService,
) {

    @PostMapping("/waiting/stores/{storeId}")
    fun registerWaiting(
        @RequestBody @Valid waitingRequest: WaitingRegisterRequest,
        @PathVariable storeId: Long,
    ): ResponseEntity<Int> {
        val waitingNumber = waitingService.register(getAuthenticationUsername(), storeId, waitingRequest)
        return ResponseEntity<Int>(waitingNumber, CREATED)
    }

}
