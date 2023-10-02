package com.handwoong.everyonewaiter.repository.menu

import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.domain.menu.QMenu.menu
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MenuRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : MenuRepositoryCustom {

    override fun findMenu(
        menuId: Long,
        categoryId: Long?,
        storeId: Long?,
    ): Menu? {
        return queryFactory.select(menu)
            .from(menu)
            .where(
                menu.id.eq(menuId),
                categoryId?.let { menu.category.id.eq(categoryId) },
                storeId?.let { menu.category.store.id.eq(storeId) },
            )
            .fetchFirst()
    }

    override fun findAllMenu(
        storeId: Long,
    ): List<Menu> {
        return queryFactory.select(menu)
            .from(menu)
            .where(menu.category.store.id.eq(storeId))
            .orderBy(menu.createdAt.asc())
            .fetch()
    }

}
