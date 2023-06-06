package com.handwoong.everyonewaiter.service.menu

import com.handwoong.everyonewaiter.dto.menu.MenuRequest
import com.handwoong.everyonewaiter.dto.menu.MenuResponse

interface MenuService {

    fun findAllStoreMenu(username: String, storeId: Long): List<MenuResponse>

    fun register(menuDto: MenuRequest, username: String, categoryId: Long)

    fun update(menuDto: MenuRequest, username: String, menuId: Long)

    fun delete(username: String, menuId: Long)

}
