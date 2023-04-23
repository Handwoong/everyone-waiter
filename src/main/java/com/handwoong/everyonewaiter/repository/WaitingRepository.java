package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.domain.WaitingStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WaitingRepository extends JpaRepository<Waiting, UUID> {

    Long countByStoreId(Long storeId);

    @Query("select w from Waiting w where w.store.id = :storeId and w.status = :status order by w.createdAt asc")
    List<Waiting> findStatusWaitingList(@Param("storeId") Long storeId,
            @Param("status") WaitingStatus status);

    @Query("select w from Waiting w where w.store.id = :storeId and w.status = :status order by w.createdAt desc")
    List<Waiting> findStatusLastWaiting(@Param("storeId") Long storeId,
            @Param("status") WaitingStatus status, Pageable pageable);

    @Query("select w from Waiting w where w.store.id = :storeId order by w.createdAt desc")
    List<Waiting> findLastWaiting(@Param("storeId") Long storeId, Pageable pageable);

    @Modifying
    @Query("update Waiting w set w.waitingTurn = w.waitingTurn - 1 where w.store.id = :storeId and w.waitingTurn > :waitingTurn and w.status = 'DEFAULT'")
    int decreaseWaitingTurn(@Param("storeId") Long storeId,
            @Param("waitingTurn") int waitingTurn);
}
