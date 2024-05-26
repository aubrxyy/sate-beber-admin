package com.decode.web.services;

import com.decode.web.dto.MenuDto;
import com.decode.web.dto.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuServices {
    void createMenu(Long categoryId, MenuDto menuDto, MultipartFile image) throws IOException;

    void updateMenu(MenuDto menuDto, MultipartFile image) throws IOException;

    List<MenuDto> findAllMenus();

    MenuDto findMenuById(Long menuId);

    void deleteMenu(Long menuId);

    List<MenuDto> searchMenus(String query);

}
