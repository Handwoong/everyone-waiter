package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Category;
import java.util.Optional;

public interface CustomCategoryRepository {

    Optional<Category> findStoreCategory(String username, Long storeId, Long categoryId);
}
