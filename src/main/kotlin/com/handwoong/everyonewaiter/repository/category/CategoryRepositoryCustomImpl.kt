package com.handwoong.everyonewaiter.repository.category

import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.domain.category.QCategory.category
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : CategoryRepositoryCustom {

    override fun findCategory(
        categoryId: Long,
        storeId: Long?,
        username: String?,
    ): Category? {
        return queryFactory.select(category)
            .from(category)
            .where(
                category.id.eq(categoryId),
                storeId?.let { category.store.id.eq(storeId) },
                username?.let { category.store.member.username.eq(username) },
            )
            .fetchFirst()
    }

    override fun findAllCategory(
        username: String,
        storeId: Long?,
    ): List<Category> {
        return queryFactory.select(category)
            .from(category)
            .where(
                category.store.member.username.eq(username),
                storeId?.let { category.store.id.eq(storeId) },
            )
            .fetch()
    }

    override fun findAllStoreCategory(storeId: Long): List<Category> {
        return queryFactory.select(category)
            .from(category)
            .where(
                category.store.id.eq(storeId),
            )
            .fetch()
    }

}
