package com.handwoong.everyonewaiter.controller.receipt

import com.handwoong.everyonewaiter.dto.receipt.ReceiptRequest
import com.handwoong.everyonewaiter.dto.receipt.ReceiptResponse
import com.handwoong.everyonewaiter.service.receipt.ReceiptService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ReceiptApiController(
    private val receiptService: ReceiptService,
) {

    @GetMapping("/stores/{storeId}/receipt")
    fun findAllStorePrint(
        @PathVariable storeId: Long,
    ): ResponseEntity<List<ReceiptResponse>> {
        val findStorePrintList = receiptService.findStoreReceiptList(storeId)
        return ResponseEntity(findStorePrintList, OK)
    }

    @PostMapping("/stores/{storeId}/receipt")
    fun register(
        @PathVariable storeId: Long,
        @RequestBody printRequest: ReceiptRequest,
    ): ResponseEntity<Unit> {
        receiptService.register(storeId, printRequest)
        return ResponseEntity(CREATED)
    }

    @PutMapping("/receipt/{receiptId}")
    fun changeStatus(
        @PathVariable receiptId: Long,
    ): ResponseEntity<Unit> {
        receiptService.changeReceiptStatus(receiptId)
        return ResponseEntity(OK)
    }

}
