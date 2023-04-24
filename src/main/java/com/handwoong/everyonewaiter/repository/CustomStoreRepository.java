package com.handwoong.everyonewaiter.repository;

public interface CustomStoreRepository {

    boolean existTelNumber(String telNumber);

    boolean existMemberStore(String username, Long storeId);
}
