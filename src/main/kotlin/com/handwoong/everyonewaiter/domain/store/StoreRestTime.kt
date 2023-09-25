package com.handwoong.everyonewaiter.domain.store

import java.time.LocalTime
import javax.persistence.Embeddable

@Embeddable
class StoreRestTime(
    var startTime: LocalTime,
    var endTime: LocalTime,
)
