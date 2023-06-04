package com.handwoong.everyonewaiter.controller.store

import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.service.store.StoreService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class StoreApiController(
    private val storeService: StoreService,
) {

    @PostMapping("/members/stores")
    fun registerStore(
        @RequestBody @Valid storeRequest: StoreRequest,
    ): ResponseEntity<Unit> {
        storeService.register(getAuthenticationUsername(), storeRequest)
        return ResponseEntity<Unit>(CREATED)
    }

    @PutMapping("/members/stores/{storeId}")
    fun updateStore(
        @RequestBody @Valid storeRequest: StoreRequest,
        @PathVariable storeId: Long,
    ): ResponseEntity<Unit> {
        storeService.update(getAuthenticationUsername(), storeId, storeRequest)
        return ResponseEntity<Unit>(OK)
    }

    @DeleteMapping("/members/stores/{storeId}")
    fun deleteStore(@PathVariable storeId: Long): ResponseEntity<Unit> {
        storeService.delete(getAuthenticationUsername(), storeId)
        return ResponseEntity<Unit>(OK)
    }

}
