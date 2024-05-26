package com.decode.web.mapper;

import com.decode.web.dto.CategoryDto;
import com.decode.web.models.Category;

import java.util.Base64;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static Category mapToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .title(categoryDto.getTitle())
                .imageUrl(categoryDto.getImageUrl())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .imageUrl(category.getImageUrl())
                .menus(category.getMenus().stream().map(MenuMapper::mapToMenuDto).collect(Collectors.toList()))
                .build();
    }
}