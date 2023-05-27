package com.handwoong.everyonewaiter.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

class ErrorResponse(
    val error: String,
    val message: String,
    val status: Int,
    val code: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {

    companion object {
        fun of(errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
            val errorResponse = ErrorResponse(
                error = errorCode.status.name,
                message = errorCode.message,
                status = errorCode.status.value(),
                code = errorCode.name,
            )
            return ResponseEntity.status(errorCode.status).body(errorResponse)
        }
    }

}
