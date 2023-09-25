package com.handwoong.everyonewaiter.service.menu

import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.dto.menu.MenuRequest
import com.handwoong.everyonewaiter.dto.menu.MenuResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.CATEGORY_NOT_FOUND
import com.handwoong.everyonewaiter.exception.ErrorCode.MENU_NOT_FOUND
import com.handwoong.everyonewaiter.repository.category.CategoryRepository
import com.handwoong.everyonewaiter.repository.menu.MenuRepository
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MenuServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
) : MenuService {

    override fun findAllStoreMenu(storeId: Long): List<MenuResponse> {
        return menuRepository.findAllMenu(storeId)
            .map(MenuResponse::of)
    }

    @Transactional
    override fun register(menuDto: MenuRequest, username: String, categoryId: Long) {
        val findCategory = findMemberCategory(username, categoryId)
        val createMenu = Menu.createMenu(menuDto, findCategory)
        findCategory.addMenu(createMenu)
    }

    @Transactional
    override fun update(menuDto: MenuRequest, menuId: Long) {
        val findMenu = findMemberMenu(menuId)
        findMenu.update(menuDto)
    }

    @Transactional
    override fun delete(menuId: Long) {
        val findMenu = findMemberMenu(menuId)
        menuRepository.delete(findMenu)
    }

    private fun findMemberCategory(username: String, categoryId: Long): Category {
        return categoryRepository.findCategory(
            categoryId = categoryId,
            username = username,
        ) ?: throwFail(CATEGORY_NOT_FOUND)
    }

    private fun findMemberMenu(menuId: Long): Menu {
        return menuRepository.findMenu(
            menuId = menuId,
        ) ?: throwFail(MENU_NOT_FOUND)
    }

}
