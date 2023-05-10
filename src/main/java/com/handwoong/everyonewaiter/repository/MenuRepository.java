package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long>, CustomMenuRepository {

    @Query("select m from Menu m where m.category.id = :categoryId and m.category.store.id = :storeId and m.category.store.member.username = :username")
    List<Menu> findAllCategoryMenu(@Param("username") String username,
            @Param("storeId") Long storeId,
            @Param("categoryId") Long categoryId);
}
