package com.handwoong.everyonewaiter.domain.order

import com.handwoong.everyonewaiter.dto.order.OrderMenuOption
import javax.persistence.Embeddable

@Embeddable
class OrderMenuOption(
    description: String,
) {

    companion object {
        fun createOrderMenuOption(
            description: String,
        ): OrderMenuOption {
            return OrderMenuOption(description)
        }
    }

}
