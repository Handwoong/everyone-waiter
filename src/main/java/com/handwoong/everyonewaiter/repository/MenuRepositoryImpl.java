package com.handwoong.everyonewaiter.repository;

import static com.handwoong.everyonewaiter.domain.QMenu.menu;

import com.handwoong.everyonewaiter.domain.Menu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Menu> findCategoryMenu(String username, Long storeId, Long categoryId,
            Long menuId) {
        return Optional.ofNullable(queryFactory
                .select(menu)
                .from(menu)
                .where(menu.id.eq(menuId),
                        menu.category.id.eq(categoryId),
                        menu.category.store.id.eq(storeId),
                        menu.category.store.member.username.eq(username))
                .fetchFirst());
    }
}
