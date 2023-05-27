package com.handwoong.everyonewaiter.dto.menu

import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.domain.menu.MenuStatus

data class MenuResponse(
    val id: Long,
    val name: String,
    val categoryName: String,
    val description: String,
    val notice: String,
    val price: Int,
    val status: MenuStatus,
    val image: String,
    val spicy: Int,
    val sortingSequence: Int,
) {

    companion object {
        fun of(menu: Menu): MenuResponse {
            return MenuResponse(
                id = menu.id!!,
                name = menu.name,
                categoryName = menu.category.name,
                description = menu.description,
                notice = menu.notice,
                price = menu.price,
                status = menu.status,
                image = menu.image,
                spicy = menu.spicy,
                sortingSequence = menu.sortingSequence,
            )
        }
    }

}
