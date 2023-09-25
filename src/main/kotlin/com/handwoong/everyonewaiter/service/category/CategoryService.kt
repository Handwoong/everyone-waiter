package com.handwoong.everyonewaiter.service.category

import com.handwoong.everyonewaiter.dto.category.CategoryRequest
import com.handwoong.everyonewaiter.dto.category.CategoryResponse

interface CategoryService {

    fun findCategoryById(username: String, categoryId: Long): CategoryResponse

    fun findAllUserCategory(username: String, storeId: Long): List<CategoryResponse>

    fun findAllStoreCategory(storeId: Long): List<CategoryResponse>

    fun register(categoryDto: CategoryRequest, username: String, storeId: Long)

    fun update(categoryDto: CategoryRequest, username: String, categoryId: Long)

    fun delete(username: String, categoryId: Long)

}
