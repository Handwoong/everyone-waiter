package com.handwoong.everyonewaiter.domain.store

import java.time.LocalTime
import javax.persistence.Embeddable

@Embeddable
class StoreBusinessTime(
    var openTime: LocalTime,
    var closeTime: LocalTime,
)
