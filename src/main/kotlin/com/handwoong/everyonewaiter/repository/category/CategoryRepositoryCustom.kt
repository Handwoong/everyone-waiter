package com.handwoong.everyonewaiter.repository.category

import com.handwoong.everyonewaiter.domain.category.Category


interface CategoryRepositoryCustom {

    fun findCategory(
        categoryId: Long,
        storeId: Long? = null,
        username: String? = null,
    ): Category?

    fun findAllCategory(
        username: String,
        storeId: Long? = null,
    ): List<Category>

    fun findAllStoreCategory(
        storeId: Long
    ): List<Category>

}
