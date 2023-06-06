package com.handwoong.everyonewaiter.dto.menu

import com.handwoong.everyonewaiter.domain.menu.MenuStatus


data class MenuRequest(
    val name: String,
    val description: String,
    val notice: String,
    val price: Int,
    val status: MenuStatus = MenuStatus.BASIC,
    val image: String,
    val spicy: Int,
    val sortingSequence: Int,
)
