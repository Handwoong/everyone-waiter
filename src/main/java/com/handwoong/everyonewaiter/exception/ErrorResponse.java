package com.handwoong.everyonewaiter.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {

    private final String error;

    private final String message;

    private final int status;

    private final String code;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(errorCode.getStatus().name())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}
