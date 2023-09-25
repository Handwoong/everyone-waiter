package com.handwoong.everyonewaiter.dto.store

import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.domain.store.StoreBusinessTime
import com.handwoong.everyonewaiter.domain.store.StoreRestTime

data class StoreResponse(
    val id: Long,
    val name: String,
    val telephone: String,
    val businessTime: StoreBusinessTime,
    val restTime: StoreRestTime,
) {

    companion object {
        fun of(store: Store): StoreResponse {
            return StoreResponse(
                id = store.id!!,
                name = store.name,
                telephone = store.telephone,
                businessTime = store.businessTime,
                restTime = store.restTime,
            )
        }
    }

}
