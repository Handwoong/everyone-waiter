package com.handwoong.everyonewaiter.service.category

import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.category.CategoryRequest
import com.handwoong.everyonewaiter.dto.category.CategoryResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.CATEGORY_NOT_FOUND
import com.handwoong.everyonewaiter.exception.ErrorCode.STORE_NOT_FOUND
import com.handwoong.everyonewaiter.repository.category.CategoryRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryServiceImpl(
    private val storeRepository: StoreRepository,
    private val categoryRepository: CategoryRepository,
) : CategoryService {

    override fun findCategoryById(username: String, categoryId: Long): CategoryResponse {
        val findCategory = findMemberCategory(categoryId, username)
        return CategoryResponse.of(findCategory)
    }

    override fun findAllUserCategory(username: String, storeId: Long): List<CategoryResponse> {
        return categoryRepository.findAllCategory(username, storeId)
            .map(CategoryResponse::of)
    }

    override fun findAllStoreCategory(storeId: Long): List<CategoryResponse> {
        return categoryRepository.findAllStoreCategory(storeId)
            .map(CategoryResponse::of)
    }

    @Transactional
    override fun register(categoryDto: CategoryRequest, username: String, storeId: Long) {
        val findStore = findMemberStore(storeId, username)
        val createCategory = Category.createCategory(categoryDto, findStore)
        categoryRepository.save(createCategory)
    }

    @Transactional
    override fun update(categoryDto: CategoryRequest, username: String, categoryId: Long) {
        val findCategory = findMemberCategory(categoryId, username)
        findCategory.update(categoryDto)
    }

    @Transactional
    override fun delete(username: String, categoryId: Long) {
        val findCategory = findMemberCategory(categoryId, username)
        categoryRepository.delete(findCategory)
    }

    private fun findMemberCategory(categoryId: Long, username: String): Category {
        return categoryRepository.findCategory(
            categoryId = categoryId,
            username = username,
        ) ?: throwFail(CATEGORY_NOT_FOUND)
    }

    private fun findMemberStore(storeId: Long, username: String): Store {
        return storeRepository.findStore(storeId, username) ?: throwFail(STORE_NOT_FOUND)
    }

}
