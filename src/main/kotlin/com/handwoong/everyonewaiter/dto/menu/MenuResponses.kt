package com.handwoong.everyonewaiter.dto.menu

import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.domain.menu.MenuCustomOption
import com.handwoong.everyonewaiter.domain.menu.MenuStatus

data class MenuResponse(
    val id: Long,
    val name: String,
    val categoryName: String,
    val description: String?,
    val notice: String?,
    val price: Int,
    val status: MenuStatus,
    val image: String?,
    val isSelectSpicy: Boolean,
    val isPrintKitchen: Boolean,
    val customOption: List<MenuCustomOptionResponse>,
    val spicy: Int,
    val sortingSequence: Int,
) {

    companion object {
        fun of(menu: Menu): MenuResponse {
            val customOptionResponses = menu.customOption.map(MenuCustomOptionResponse::of)

            return MenuResponse(
                id = menu.id!!,
                name = menu.name,
                categoryName = menu.category.name,
                description = menu.description,
                notice = menu.notice,
                price = menu.price,
                status = menu.status,
                image = menu.image,
                isSelectSpicy = menu.option.isSelectSpicy,
                isPrintKitchen = menu.option.isPrintKitchen,
                customOption = customOptionResponses,
                spicy = menu.spicy,
                sortingSequence = menu.sortingSequence,
            )
        }
    }

}

data class MenuCustomOptionResponse(
    val id: Long,
    val name: String,
    val price: Int,
) {

    companion object {
        fun of(
            menuCustomOption: MenuCustomOption,
        ): MenuCustomOptionResponse {
            return MenuCustomOptionResponse(
                id = menuCustomOption.id!!,
                name = menuCustomOption.name,
                price = menuCustomOption.price,
            )
        }
    }

}
