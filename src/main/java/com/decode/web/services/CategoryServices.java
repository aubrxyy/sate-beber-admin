package com.decode.web.services;

import com.decode.web.dto.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryServices {
    List<CategoryDto> findAllCategories();

    void saveCategory(CategoryDto category, MultipartFile image) throws IOException;

    CategoryDto findCategoryById(long categoryId);

    void updateCategory(CategoryDto category, MultipartFile image) throws IOException;

    void delete(Long categoryId);


}
