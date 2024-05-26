package com.decode.web.Controller;

import com.decode.web.dto.CategoryDto;
import com.decode.web.dto.MenuDto;
import com.decode.web.models.Category;
import com.decode.web.services.CategoryServices;
import com.decode.web.services.MenuServices;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MenuController {
    private final MenuServices menuServices;
    private final CategoryServices categoryServices;

    public MenuController(MenuServices menuServices, CategoryServices categoryServices) {
        this.menuServices = menuServices;
        this.categoryServices = categoryServices;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/menus";
    }

    @GetMapping("/menus")
    public String listMenus(Model model) {
        List<MenuDto> menus = menuServices.findAllMenus();
        model.addAttribute("menus", menus);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "menus-list";
    }

    @GetMapping("/menus/{categoryId}/new")
    public String createMenuForm(@PathVariable("categoryId") Long categoryId, Model model) {
        MenuDto menuDto = new MenuDto();
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("menu", menuDto);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "menus-create";
    }

    @GetMapping("/menus/{menuId}/edit")
    public String editMenuForm(@PathVariable("menuId") Long menuId, Model model) {
        MenuDto menu = menuServices.findMenuById(menuId);
        model.addAttribute("menu", menu);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "menus-edit";
    }

    @PostMapping("/menus/{menuId}/edit")
    public String updateMenu(@PathVariable("menuId") Long menuId,
                             @Valid @ModelAttribute("menu") MenuDto menu, @RequestParam("image") MultipartFile image,
                             BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("menu", menu);
            List<CategoryDto> categories = categoryServices.findAllCategories();
            model.addAttribute("categories", categories);
            return "menus-edit";
        }
        try {
            if (image != null && !image.isEmpty()) {
                menu.setId(menuId);
                menuServices.updateMenu(menu, image);
            } else {
                menuServices.updateMenu(menu, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "menus-edit";
        }
        return "redirect:/menus";
    }

    @PostMapping("/menus/{categoryId}")
    public String createMenu(@PathVariable("categoryId") Long categoryId,
                             @Valid @ModelAttribute("menu") MenuDto menuDto,
                             @RequestParam("image") MultipartFile image,
                             BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("categoryId", categoryId);
            return "menus-create";
        }
        CategoryDto categoryDto = categoryServices.findCategoryById(categoryId);
        if (categoryDto != null && categoryDto.getId() != null) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setTitle(categoryDto.getTitle());
            menuDto.setCategory(category);
            menuServices.createMenu(categoryId, menuDto, image);
            return "redirect:/categories/" + categoryId;
        } else {
            model.addAttribute("error", "Invalid category ID");
            return "menus-create";
        }
    }

    @GetMapping("/menus/{menuId}/delete")
    public String deleteMenu(@PathVariable("menuId") Long menuId) {
        menuServices.deleteMenu(menuId);
        return "redirect:/menus";
    }

    @GetMapping("/menus/search")
    public String searchMenus(@RequestParam(value = "query") String query, Model model) {
        List<MenuDto> menus = menuServices.searchMenus(query);
        model.addAttribute("menus", menus);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "menus-list";
    }
}
