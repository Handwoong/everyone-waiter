package com.handwoong.everyonewaiter.domain.menu

import javax.persistence.Embeddable

@Embeddable
class MenuOption(
    var isSelectSpicy: Boolean,
    var isPrintKitchen: Boolean,
)
