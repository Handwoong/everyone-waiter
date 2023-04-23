package com.handwoong.everyonewaiter.exception;

public class ResourceNotMatchException extends RuntimeException {

    public ResourceNotMatchException() {
    }

    public ResourceNotMatchException(String message) {
        super(message);
    }

    public ResourceNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotMatchException(Throwable cause) {
        super(cause);
    }

    public ResourceNotMatchException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
