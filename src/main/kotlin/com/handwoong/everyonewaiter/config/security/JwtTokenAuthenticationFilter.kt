package com.handwoong.everyonewaiter.config.security

import com.handwoong.everyonewaiter.util.createCookie
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : GenericFilterBean() {

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        val cookieToken = getRequestAccessTokenCookie(request as HttpServletRequest)
        val authentication = saveAuthentication(cookieToken?.value)
        setAccessTokenCookie(response as HttpServletResponse, authentication)
        chain.doFilter(request, response)
    }

    private fun getRequestAccessTokenCookie(request: HttpServletRequest): Cookie? {
        return request.cookies?.firstOrNull { cookie -> cookie.name == "token" }
    }

    private fun saveAuthentication(accessToken: String?): Authentication? {
        return if ((accessToken != null) && jwtTokenProvider.validateToken(accessToken)) {
            val authentication = jwtTokenProvider.getAuthentication(accessToken)
            SecurityContextHolder.getContext().authentication = authentication
            authentication
        } else {
            null
        }
    }

    private fun setAccessTokenCookie(
        response: HttpServletResponse,
        authentication: Authentication?,
    ) {
        authentication?.let {
            val newToken = jwtTokenProvider.generateToken(it)
            response.addCookie(createCookie(newToken.accessToken))
        }
    }

}
