package com.handwoong.everyonewaiter.config.security

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.handwoong.everyonewaiter.exception.ErrorCode.UN_AUTHORIZED
import com.handwoong.everyonewaiter.exception.ErrorResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationEntryPointCustom : AuthenticationEntryPoint {

    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val errorResponse = ErrorResponse.of(UN_AUTHORIZED)
        val responseBody = mapper.writeValueAsString(errorResponse.body)

        response.characterEncoding = "utf-8"
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = UN_AUTHORIZED.status.value()
        response.writer.write(responseBody)
    }

}
