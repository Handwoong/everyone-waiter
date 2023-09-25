package com.handwoong.everyonewaiter.dto.category

import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.dto.menu.MenuResponse

data class CategoryResponse(
    val id: Long,
    val name: String,
    val icon: String,
    val menuList: List<MenuResponse>,
) {

    companion object {
        fun of(category: Category): CategoryResponse {
            val menuDtoList = category.menuList
                .map { menu -> MenuResponse.of(menu) }

            return CategoryResponse(
                id = category.id!!,
                name = category.name,
                icon = category.icon,
                menuList = menuDtoList,
            )
        }
    }

}
