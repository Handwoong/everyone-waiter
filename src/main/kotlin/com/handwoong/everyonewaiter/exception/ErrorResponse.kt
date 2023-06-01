package com.handwoong.everyonewaiter.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ErrorResponse(
    val error: String,
    val message: String,
    val status: Int,
    val code: String,
    val timestamp: LocalDateTime,
) {

    companion object {
        fun of(errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
            val errorResponse = ErrorResponse(
                error = errorCode.status.name,
                message = errorCode.message,
                status = errorCode.status.value(),
                code = errorCode.name,
                timestamp = LocalDateTime.now(),
            )
            return ResponseEntity.status(errorCode.status).body(errorResponse)
        }
    }

}
