package com.handwoong.everyonewaiter.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.annotation.Inherited

@Inherited
@Target(
    AnnotationTarget.FUNCTION,
)
@Retention(
    AnnotationRetention.RUNTIME,
)
annotation class ExcludeLog

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
