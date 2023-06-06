package com.handwoong.everyonewaiter.util

import com.handwoong.everyonewaiter.exception.ErrorCode.UN_AUTHORIZED
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.http.Cookie

const val ONE_DAY_HOUR = 24
const val HOUR_SECONDS = 3600

fun getAuthentication(): Authentication? {
    return SecurityContextHolder.getContext().authentication
}

fun getAuthenticationUsername(): String {
    val authentication = getAuthentication()
    if (authentication != null && isAuthentication()) {
        return authentication.name
    }
    throwFail(UN_AUTHORIZED)
}

fun isAuthentication(): Boolean {
    val authentication = getAuthentication()
    return if (authentication != null) {
        authentication.principal !is String
    } else {
        false
    }
}

fun createCookie(accessToken: String): Cookie {
    val cookie = Cookie("token", accessToken)
    cookie.path = "/"
    cookie.maxAge = ONE_DAY_HOUR * HOUR_SECONDS
    cookie.isHttpOnly = true
//    cookie.secure = true
    return cookie
}
