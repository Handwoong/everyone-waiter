package com.handwoong.everyonewaiter.repository.store

import com.handwoong.everyonewaiter.domain.store.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<Store, Long>, StoreRepositoryCustom
