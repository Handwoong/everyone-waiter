package com.handwoong.everyonewaiter.service;

import static com.handwoong.everyonewaiter.enums.ErrorCode.CATEGORY_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.MENU_NOT_FOUND;
import static com.handwoong.everyonewaiter.enums.ErrorCode.STORE_NOT_FOUND;

import com.handwoong.everyonewaiter.domain.Category;
import com.handwoong.everyonewaiter.domain.Menu;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.CategoryDto;
import com.handwoong.everyonewaiter.dto.CategoryDto.RequestDto;
import com.handwoong.everyonewaiter.dto.MenuDto;
import com.handwoong.everyonewaiter.exception.CustomException;
import com.handwoong.everyonewaiter.repository.CategoryRepository;
import com.handwoong.everyonewaiter.repository.MenuRepository;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final CategoryRepository categoryRepository;

    private final MenuRepository menuRepository;

    private final StoreRepository storeRepository;

    @Override
    public List<CategoryDto.ResponseDto> findAllCategory(String username, Long storeId) {
        return categoryRepository.findAllCategory(username, storeId)
                .stream()
                .map(CategoryDto.ResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public Long categoryRegister(RequestDto categoryDto, String username, Long storeId) {
        Store findStore = findMemberStore(username, storeId);

        Category category = Category.createCategory(findStore, categoryDto);
        categoryRepository.save(category);

        return category.getId();
    }

    @Override
    @Transactional
    public void categoryUpdate(RequestDto categoryDto, String username, Long storeId,
            Long categoryId) {
        Category findCategory = findStoreCategory(username, storeId, categoryId);
        findCategory.update(categoryDto);
    }

    @Override
    @Transactional
    public void categoryDelete(String username, Long storeId, Long categoryId) {
        Category findCategory = findStoreCategory(username, storeId, categoryId);
        categoryRepository.delete(findCategory);
    }

    @Override
    public List<MenuDto.ResponseDto> findAllCategoryMenu(String username, Long storeId,
            Long categoryId) {
        return menuRepository.findAllCategoryMenu(username, storeId, categoryId)
                .stream()
                .map(MenuDto.ResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public Long menuRegister(MenuDto.RequestDto menuDto, String username, Long storeId,
            Long categoryId) {
        Category findCategory = findStoreCategory(username, storeId, categoryId);

        Menu menu = Menu.createMenu(findCategory, menuDto);
        findCategory.addMenu(menu);

        return menu.getId();
    }

    @Override
    @Transactional
    public void menuUpdate(MenuDto.RequestDto menuDto, String username, Long storeId,
            Long categoryId, Long menuId) {
        Menu findMenu = findCategoryMenu(username, storeId, categoryId, menuId);
        findMenu.update(menuDto);
    }

    @Override
    @Transactional
    public void menuDelete(String username, Long storeId, Long categoryId, Long menuId) {
        Menu findMenu = findCategoryMenu(username, storeId, categoryId, menuId);
        menuRepository.delete(findMenu);
    }

    private Store findMemberStore(String username, Long storeId) {
        return storeRepository.findMemberStore(username, storeId)
                .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
    }

    private Category findStoreCategory(String username, Long storeId, Long categoryId) {
        return categoryRepository.findStoreCategory(username, storeId, categoryId)
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));
    }

    private Menu findCategoryMenu(String username, Long storeId, Long categoryId, Long menuId) {
        return menuRepository.findCategoryMenu(username, storeId, categoryId, menuId)
                .orElseThrow(() -> new CustomException(MENU_NOT_FOUND));
    }
}
