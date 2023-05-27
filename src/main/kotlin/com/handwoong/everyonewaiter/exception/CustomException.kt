package com.handwoong.everyonewaiter.exception

class CustomException(
    val errorCode: ErrorCode,
) : RuntimeException()
