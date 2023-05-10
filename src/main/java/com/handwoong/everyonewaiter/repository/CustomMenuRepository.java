package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Menu;
import java.util.Optional;

public interface CustomMenuRepository {

    Optional<Menu> findCategoryMenu(String username, Long storeId, Long categoryId, Long menuId);
}
