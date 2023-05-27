package com.handwoong.everyonewaiter.util

import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode
import com.handwoong.everyonewaiter.exception.ErrorCode.ID_RESOURCE_NOT_FOUND
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

fun throwFail(errorCode: ErrorCode): Nothing {
    throw CustomException(errorCode)
}

fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T {
    return this.findByIdOrNull(id) ?: throw CustomException(ID_RESOURCE_NOT_FOUND)
}
