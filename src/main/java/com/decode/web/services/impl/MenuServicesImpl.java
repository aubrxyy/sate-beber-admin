package com.decode.web.services.impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.decode.web.dto.MenuDto;
import com.decode.web.mapper.CategoryMapper;
import com.decode.web.mapper.MenuMapper;
import com.decode.web.models.Category;
import com.decode.web.models.Menu;
import com.decode.web.repository.MenuRepository;
import com.decode.web.services.MenuServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuServicesImpl implements MenuServices {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final MenuRepository menuRepository;

    public MenuServicesImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void createMenu(Long categoryId, MenuDto menuDto, MultipartFile image) throws IOException {
        Menu menu = MenuMapper.mapToMenu(menuDto);
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToAzure(image);
            menu.setImageUrl(imageUrl);
        }
        menuRepository.save(menu);
    }

    @Override
    public void updateMenu(MenuDto menuDto, MultipartFile image) throws IOException {
        Menu menu = menuRepository.findById(menuDto.getId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        menu.setTitle(menuDto.getTitle());
        menu.setPrice(menuDto.getPrice());
        menu.setDescription(menuDto.getDescription());
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadImageToAzure(image);
            menu.setImageUrl(imageUrl);
        }
        menuRepository.save(menu);
    }

    @Override
    public List<MenuDto> findAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuMapper::mapToMenuDto).collect(Collectors.toList());
    }

    @Override
    public MenuDto findMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId).get();
        return MenuMapper.mapToMenuDto(menu);
    }

    @Override
    public void deleteMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }

    @Override
    public List<MenuDto> searchMenus(String query) {
        List<Menu> menus = menuRepository.searchMenus(query);
        return menus.stream().map(MenuMapper::mapToMenuDto).collect(Collectors.toList());
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

