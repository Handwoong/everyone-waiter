package com.handwoong.everyonewaiter.exception

import com.handwoong.everyonewaiter.util.logger
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    private val log = logger()

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(error: CustomException): ResponseEntity<ErrorResponse> {
        val errorCode = error.errorCode
        log.error("[${errorCode.name}] ${errorCode.status} ${errorCode.message}")
        return ErrorResponse.of(errorCode)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleNotValidException(error: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID
        log.error("[${errorCode.name}] ${errorCode.status} ${errorCode.message} ${error.message}")
        return ErrorResponse.of(errorCode)
    }

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handleBadCredentialsException(error: BadCredentialsException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.CHECK_USERNAME_AND_PASSWORD
        log.error("[${errorCode.name}] ${errorCode.status} ${errorCode.message}")
        return ErrorResponse.of(errorCode)
    }

    @ExceptionHandler(value = [InternalAuthenticationServiceException::class])
    fun handleInternalAuthenticationServiceException(error: InternalAuthenticationServiceException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.CHECK_USERNAME_AND_PASSWORD
        log.error("[${errorCode.name}] ${errorCode.status} ${errorCode.message}")
        return ErrorResponse.of(errorCode)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(error: Exception): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.SERVER_ERROR
        log.error("[${errorCode.name}] ${errorCode.status} ${errorCode.message}")
        log.error("${error}")
        return ErrorResponse.of(errorCode)
    }

}
