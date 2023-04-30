package com.handwoong.everyonewaiter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.handwoong.everyonewaiter.api")
public class ApiExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public Object handleCustomException(CustomException err) {
        ErrorCode errorCode = err.getErrorCode();
        log.error("[{}] {} {}", errorCode.name(), errorCode.getStatus(), errorCode.getMessage());
        return ErrorResponse.toResponseEntity(errorCode);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Object handleCustomException(MethodArgumentNotValidException err) {
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        log.error("[{}] {} {} {}", errorCode.name(), errorCode.getStatus(), errorCode.getMessage(),
                err.getDetailMessageArguments());
        return ErrorResponse.toResponseEntity(errorCode);
    }

    @ExceptionHandler({Exception.class})
    public Object handleException(Exception err) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("[{}] {} {} {}",
                errorCode.name(), errorCode.getStatus(), errorCode.getMessage(), err.getMessage());
        return ErrorResponse.toResponseEntity(errorCode);
    }
}
