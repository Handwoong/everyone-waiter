package com.handwoong.everyonewaiter.repository.store

import com.handwoong.everyonewaiter.domain.store.Store


interface StoreRepositoryCustom {

    fun existsStore(
        username: String,
        telephone: String,
    ): Boolean

    fun findStore(
        storeId: Long,
        username: String? = null,
    ): Store?

    fun findAllStore(
        username: String,
    ): List<Store>

}
