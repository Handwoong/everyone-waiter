package com.handwoong.everyonewaiter.util

import java.lang.annotation.Inherited

@Inherited
@Target(
    AnnotationTarget.FUNCTION,
)
@Retention(
    AnnotationRetention.RUNTIME,
)
annotation class ExcludeLog
