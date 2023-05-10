package com.handwoong.everyonewaiter.service;

import com.handwoong.everyonewaiter.dto.CategoryDto;
import com.handwoong.everyonewaiter.dto.MenuDto;
import java.util.List;

public interface MenuService {

    List<CategoryDto.ResponseDto> findAllCategory(String username, Long storeId);

    Long categoryRegister(CategoryDto.RequestDto categoryDto, String username, Long storeId);

    void categoryUpdate(CategoryDto.RequestDto categoryDto, String username, Long storeId,
            Long categoryId);

    void categoryDelete(String username, Long storeId, Long categoryId);

    List<MenuDto.ResponseDto> findAllCategoryMenu(String username, Long storeId, Long categoryId);

    Long menuRegister(MenuDto.RequestDto menuDto, String username, Long storeId, Long categoryId);

    void menuUpdate(MenuDto.RequestDto menuDto, String username, Long storeId, Long categoryId,
            Long menuId);

    void menuDelete(String username, Long storeId, Long categoryId, Long menuId);
}
