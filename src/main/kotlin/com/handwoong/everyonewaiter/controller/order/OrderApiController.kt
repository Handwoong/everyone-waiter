package com.handwoong.everyonewaiter.controller.order

import com.handwoong.everyonewaiter.dto.order.*
import com.handwoong.everyonewaiter.service.order.OrderService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OrderApiController(
    private val orderService: OrderService,
) {

    @GetMapping("/api/stores/{storeId}/orders")
    fun counterOrderList(
        @PathVariable storeId: Long,
    ): ResponseEntity<List<OrderResponses>> {
        val findAllStoreOrderList = orderService.findAllStoreOrder(storeId)
        return ResponseEntity(findAllStoreOrderList, OK)
    }

    @PostMapping("/stores/{storeId}/orders")
    fun registerOrder(
        @PathVariable storeId: Long,
        @RequestBody orderRequest: OrderRequests,
    ): ResponseEntity<Unit> {
        orderService.register(storeId, orderRequest)
        return ResponseEntity<Unit>(CREATED)
    }

    @PostMapping("/stores/{storeId}/order/call")
    fun registerOrderCall(
        @PathVariable storeId: Long,
        @RequestBody orderCallRequest: OrderCallRequest,
    ): ResponseEntity<Unit> {
        orderService.callRegister(storeId, orderCallRequest)
        return ResponseEntity<Unit>(CREATED)
    }

    @PutMapping("/stores/{storeId}/orders/{orderId}/serve")
    fun changeStatusAllOrder(
        @PathVariable storeId: Long,
        @PathVariable orderId: Long,
    ): ResponseEntity<Unit> {
        orderService.changeStatusAllOrderMenu(storeId, orderId)
        return ResponseEntity<Unit>(OK)
    }

    @PutMapping("/stores/{storeId}/orders/{orderId}/serve/{orderMenuId}")
    fun changeStatusOrder(
        @PathVariable storeId: Long,
        @PathVariable orderId: Long,
        @PathVariable orderMenuId: Long,
    ): ResponseEntity<Unit> {
        orderService.changeStatusOrderMenu(storeId, orderId, orderMenuId)
        return ResponseEntity<Unit>(OK)
    }

    @PutMapping("/stores/{storeId}/order/call/{orderCallId}")
    fun changeStatusOrderCall(
        @PathVariable storeId: Long,
        @PathVariable orderCallId: Long,
    ): ResponseEntity<Unit> {
        orderService.changeStatusOrderCall(storeId, orderCallId)
        return ResponseEntity(OK)
    }

    @PutMapping("/stores/{storeId}/table/before/{beforeTableNumber}/after/{afterTableNumber}")
    fun changeTableNumber(
        @PathVariable storeId: Long,
        @PathVariable beforeTableNumber: Int,
        @PathVariable afterTableNumber: Int,
    ): ResponseEntity<Unit> {
        orderService.changeOrderTableNumber(storeId, beforeTableNumber, afterTableNumber)
        return ResponseEntity<Unit>(OK)
    }

    @PutMapping("/stores/{storeId}/discount")
    fun orderDiscount(
        @PathVariable storeId: Long,
        @RequestBody discountRequest: DiscountRequest,
    ): ResponseEntity<Unit> {
        orderService.discount(storeId, discountRequest)
        return ResponseEntity<Unit>(OK)
    }

    @PutMapping("/stores/{storeId}/order/menu/qty")
    fun changeOrderMenuQty(
        @PathVariable storeId: Long,
        @RequestBody orderMenuQtyRequest: OrderMenuQtyRequest,
    ): ResponseEntity<Unit> {
        orderService.changeOrderMenuQty(storeId, orderMenuQtyRequest)
        return ResponseEntity<Unit>(OK)
    }

    @DeleteMapping("/stores/{storeId}/order/{orderId}/menu/{orderMenuId}")
    fun deleteOrderMenu(
        @PathVariable storeId: Long,
        @PathVariable orderId: Long,
        @PathVariable orderMenuId: Long,
    ): ResponseEntity<Unit> {
        orderService.deleteOrderMenu(storeId, orderId, orderMenuId)
        return ResponseEntity<Unit>(OK)
    }

}
