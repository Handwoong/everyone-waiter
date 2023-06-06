package com.handwoong.everyonewaiter.repository.menu

import com.handwoong.everyonewaiter.domain.menu.Menu


interface MenuRepositoryCustom {

    fun findMenu(
        menuId: Long,
        categoryId: Long? = null,
        storeId: Long? = null,
        username: String? = null,
    ): Menu?

    fun findAllMenu(
        username: String,
        storeId: Long? = null,
    ): List<Menu>

}
