package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByMemberId(Long memberId);
}
