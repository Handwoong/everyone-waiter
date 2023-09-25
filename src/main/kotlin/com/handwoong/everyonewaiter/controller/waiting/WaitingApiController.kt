package com.handwoong.everyonewaiter.controller.waiting

import com.handwoong.everyonewaiter.dto.waiting.WaitingRegisterRequest
import com.handwoong.everyonewaiter.service.waiting.WaitingService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
class WaitingApiController(
    private val waitingService: WaitingService,
) {

    @PostMapping("/stores/{storeId}/waiting")
    fun registerWaiting(
        @RequestBody @Valid waitingRequest: WaitingRegisterRequest,
        @PathVariable storeId: Long,
    ): ResponseEntity<Int> {
        val waitingNumber = waitingService.register(getAuthenticationUsername(), storeId, waitingRequest)
        return ResponseEntity<Int>(waitingNumber, CREATED)
    }

    @PostMapping("/stores/{storeId}/waiting/send/{waitingId}")
    fun sendEnterMessage(
        @PathVariable waitingId: UUID,
        @PathVariable storeId: Long,
    ): ResponseEntity<Unit> {
        waitingService.sendWaitingEnterMessage(waitingId)
        return ResponseEntity<Unit>(OK)
    }

    @DeleteMapping("/stores/{storeId}/waiting/cancel/{waitingId}")
    fun cancelWaiting(
        @PathVariable waitingId: UUID,
        @PathVariable storeId: Long,
    ): ResponseEntity<Unit> {
        waitingService.cancelWaiting(storeId, waitingId)
        return ResponseEntity<Unit>(OK)
    }

    @DeleteMapping("/stores/{storeId}/waiting/{waitingId}")
    fun enterWaiting(
        @PathVariable waitingId: UUID,
        @PathVariable storeId: Long,
    ): ResponseEntity<Unit> {
        waitingService.enterWaiting(getAuthenticationUsername(), storeId, waitingId)
        return ResponseEntity<Unit>(OK)
    }

}
