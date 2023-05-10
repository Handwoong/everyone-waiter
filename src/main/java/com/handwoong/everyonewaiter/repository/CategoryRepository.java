package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        CustomCategoryRepository {

    @Query("select c from Category c where c.store.id = :storeId and c.store.member.username = :username")
    List<Category> findAllCategory(@Param("username") String username,
            @Param("storeId") Long storeId);
}
