package com.handwoong.everyonewaiter.util

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

fun getAuthentication(): Authentication {
    return SecurityContextHolder.getContext().authentication
}

fun getUsername(): String {
    return SecurityContextHolder.getContext().authentication.name
}

fun isAuthentication(): Boolean {
    val authentication = getAuthentication()
    return authentication.principal !is String
}
