package com.handwoong.everyonewaiter.util

import java.lang.annotation.Inherited
import java.util.concurrent.TimeUnit

@Inherited
@Target(
    AnnotationTarget.FUNCTION,
)
@Retention(
    AnnotationRetention.RUNTIME,
)
annotation class RedissonLock(
    val key: String,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    val waitTime: Long = 5L,
    val leaseTime: Long = 3L,
)
