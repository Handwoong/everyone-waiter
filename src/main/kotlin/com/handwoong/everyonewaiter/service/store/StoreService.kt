package com.handwoong.everyonewaiter.service.store

import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.dto.store.StoreResponse

interface StoreService {

    fun register(username: String, storeDto: StoreRequest)

    fun update(username: String, storeId: Long, storeDto: StoreRequest)

    fun delete(username: String, storeId: Long)

    fun findStore(username: String, storeId: Long): StoreResponse

    fun findStoreById(storeId: Long): StoreResponse

    fun findStoreList(username: String): List<StoreResponse>

}
