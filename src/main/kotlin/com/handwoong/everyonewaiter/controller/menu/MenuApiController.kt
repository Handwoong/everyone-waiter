package com.handwoong.everyonewaiter.controller.menu

import com.handwoong.everyonewaiter.dto.category.CategoryRequest
import com.handwoong.everyonewaiter.dto.menu.MenuRequest
import com.handwoong.everyonewaiter.dto.menu.MenuResponse
import com.handwoong.everyonewaiter.service.category.CategoryService
import com.handwoong.everyonewaiter.service.menu.MenuService
import com.handwoong.everyonewaiter.util.getAuthenticationUsername
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class MenuApiController(
    private val categoryService: CategoryService,
    private val menuService: MenuService,
) {

    @GetMapping("/menus/{storeId}")
    fun findMenu(
        @PathVariable storeId: Long,
    ): ResponseEntity<List<MenuResponse>> {
        val findAllStoreMenu = menuService.findAllStoreMenu(storeId)
        return ResponseEntity(findAllStoreMenu, OK)
    }

    @PostMapping("/members/stores/{storeId}/category")
    fun registerCategory(
        @PathVariable storeId: Long,
        @RequestBody @Valid categoryRequest: CategoryRequest,
    ): ResponseEntity<Unit> {
        categoryService.register(categoryRequest, getAuthenticationUsername(), storeId)
        return ResponseEntity<Unit>(CREATED)
    }

    @PostMapping("/members/category/{categoryId}/menus")
    fun registerMenu(
        @PathVariable categoryId: Long,
        @RequestBody @Valid menuRequest: MenuRequest,
    ): ResponseEntity<Unit> {
        menuService.register(menuRequest, getAuthenticationUsername(), categoryId)
        return ResponseEntity<Unit>(CREATED)
    }

    @PutMapping("/members/menus/{menuId}")
    fun editMenu(
        @PathVariable menuId: Long,
        @RequestBody @Valid menuRequest: MenuRequest,
    ): ResponseEntity<Unit> {
        menuService.update(menuRequest, menuId)
        return ResponseEntity<Unit>(OK)
    }

    @DeleteMapping("/members/menus/{menuId}")
    fun deleteMenu(
        @PathVariable menuId: Long,
    ): ResponseEntity<Unit> {
        menuService.delete(menuId)
        return ResponseEntity<Unit>(OK)
    }

}
