package com.handwoong.everyonewaiter.repository;

import static com.handwoong.everyonewaiter.domain.QStore.store;

import com.handwoong.everyonewaiter.domain.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class StoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existTelNumber(String username, String telNumber) {
        Store findStore = queryFactory
                .select(store)
                .from(store)
                .where(neUsername(username), store.telephoneNumber.eq(telNumber))
                .fetchFirst();
        return findStore != null;
    }

    @Override
    public Optional<Store> findMemberStore(String username, Long storeId) {
        return Optional.ofNullable(queryFactory
                .select(store)
                .from(store)
                .where(store.member.username.eq(username), store.id.eq(storeId))
                .fetchFirst());
    }

    private BooleanExpression neUsername(String username) {
        return username != null ? store.member.username.ne(username) : null;
    }
}
