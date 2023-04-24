package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Store;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {

    @Query("select s from Store s where s.member.username = :username and s.id = :storeId")
    List<Store> findMemberStoreList(@Param("username") String username,
            @Param("storeId") Long storeId, Pageable pageable);

    @Query("select s from Store s where s.member.username = :username and s.id = :storeId")
    Page<Store> findMemberStorePage(@Param("username") String username,
            @Param("storeId") Long storeId, Pageable pageable);

    Store findStoreById(Long storeId);

    List<Store> findAllByMemberId(Long memberId);
}
