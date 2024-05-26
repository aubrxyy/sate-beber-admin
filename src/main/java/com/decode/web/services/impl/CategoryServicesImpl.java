package com.decode.web.services.impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.decode.web.dto.CategoryDto;
import com.decode.web.mapper.CategoryMapper;
import com.decode.web.models.Category;
import com.decode.web.repository.CategoryRepository;
import com.decode.web.services.CategoryServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServicesImpl implements CategoryServices {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final CategoryRepository categoryRepository;

    public CategoryServicesImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public void saveCategory(CategoryDto categoryDto, MultipartFile image) throws IOException {
        Category category = CategoryMapper.mapToCategory(categoryDto);
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToAzure(image);
            category.setImageUrl(imageUrl);
        }
        categoryRepository.save(category);
    }

    @Override
    public CategoryDto findCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setTitle(categoryDto.getTitle());
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToAzure(image);
            category.setImageUrl(imageUrl);
        }
        categoryRepository.save(category);
    }

    @Override
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private String uploadImageToAzure(MultipartFile file) throws IOException {
        BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

        String blobName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        containerClient.getBlobClient(blobName).upload(file.getInputStream(), file.getSize(), true);

        return containerClient.getBlobClient(blobName).getBlobUrl();
    }
}


