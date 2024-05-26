package com.decode.web.mapper;

import com.decode.web.dto.MenuDto;
import com.decode.web.models.Menu;

import java.util.Base64;

public class MenuMapper {
    public static Menu mapToMenu(MenuDto menuDto) {
        return Menu.builder()
                .id(menuDto.getId())
                .title(menuDto.getTitle())
                .price(menuDto.getPrice())
                .description(menuDto.getDescription())
                .imageUrl(menuDto.getImageUrl())
                .category(menuDto.getCategory())
                .build();
    }

    public static MenuDto mapToMenuDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .title(menu.getTitle())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .imageUrl(menu.getImageUrl())
                .category(menu.getCategory())
                .build();
    }
}
