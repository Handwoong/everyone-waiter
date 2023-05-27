package com.handwoong.everyonewaiter.repository.store

import com.handwoong.everyonewaiter.domain.store.QStore.store
import com.handwoong.everyonewaiter.domain.store.Store
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class StoreRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : StoreRepositoryCustom {

    override fun existsStore(
        username: String,
        telephone: String,
    ): Boolean {
        val findStore = queryFactory.select(store)
            .from(store)
            .where(
                store.member.username.ne(username),
                store.telephone.eq(telephone),
            )
            .fetchFirst()
        return findStore != null
    }

    override fun findStore(
        storeId: Long,
        username: String?,
    ): Store? {
        return queryFactory.select(store)
            .from(store)
            .where(
                store.id.eq(storeId),
                username?.let { store.member.username.eq(username) },
            )
            .fetchFirst()
    }

    override fun findAllStore(
        username: String,
    ): List<Store> {
        return queryFactory.select(store)
            .from(store)
            .where(
                store.member.username.eq(username),
            )
            .fetch()
    }

}
