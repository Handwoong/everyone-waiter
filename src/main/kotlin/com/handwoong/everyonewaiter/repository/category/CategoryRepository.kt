package com.handwoong.everyonewaiter.repository.category

import com.handwoong.everyonewaiter.domain.category.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>, CategoryRepositoryCustom
