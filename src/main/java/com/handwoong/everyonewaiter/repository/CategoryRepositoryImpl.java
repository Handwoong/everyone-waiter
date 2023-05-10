package com.handwoong.everyonewaiter.repository;

import static com.handwoong.everyonewaiter.domain.QCategory.category;

import com.handwoong.everyonewaiter.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl implements CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Category> findStoreCategory(String username, Long storeId, Long categoryId) {
        return Optional.ofNullable(queryFactory
                .select(category)
                .from(category)
                .where(category.store.member.username.eq(username),
                        category.store.id.eq(storeId),
                        category.id.eq(categoryId))
                .fetchFirst());
    }
}
