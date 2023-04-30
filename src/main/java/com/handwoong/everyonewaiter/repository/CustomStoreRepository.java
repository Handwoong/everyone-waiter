package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Store;
import java.util.Optional;

public interface CustomStoreRepository {

    boolean existTelNumber(String username, String telNumber);

    Optional<Store> findMemberStore(String username, Long storeId);
}
