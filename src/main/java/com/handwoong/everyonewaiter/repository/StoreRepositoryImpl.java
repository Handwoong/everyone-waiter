package com.handwoong.everyonewaiter.repository;

import static com.handwoong.everyonewaiter.domain.QStore.store;

import com.handwoong.everyonewaiter.domain.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class StoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existTelNumber(String telNumber) {
        Store findStore = queryFactory
                .select(store)
                .from(store)
                .where(store.telephoneNumber.eq(telNumber))
                .fetchFirst();
        return findStore != null;
    }

    @Override
    public boolean existMemberStore(String username, Long storeId) {
        Store findStore = queryFactory
                .select(store)
                .from(store)
                .where(store.member.username.eq(username), store.id.eq(storeId))
                .fetchFirst();
        return findStore != null;
    }
}
