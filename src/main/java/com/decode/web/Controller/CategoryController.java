package com.decode.web.Controller;

import com.decode.web.dto.CategoryDto;
import com.decode.web.services.CategoryServices;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryServices categoryServices;

    public CategoryController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping("/{categoryId}")
    public String categoryDetail(@PathVariable("categoryId") Long categoryId, Model model) {
        CategoryDto categoryDto = categoryServices.findCategoryById(categoryId);
        model.addAttribute("category", categoryDto);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories-detail";
    }

    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories-create";
    }

    @PostMapping("/new")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto,
                               BindingResult result,
                               @RequestParam("image") MultipartFile image,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            List<CategoryDto> categories = categoryServices.findAllCategories();
            model.addAttribute("categories", categories);
            return "categories-create";
        }
        try {
            categoryServices.saveCategory(categoryDto, image);
        } catch (IOException e) {
            e.printStackTrace();
            return "categories-create";
        }
        return "redirect:/menus";
    }

    @GetMapping("/{categoryId}/edit")
    public String editCategoryForm(@PathVariable("categoryId") Long categoryId, Model model) {
        CategoryDto category = categoryServices.findCategoryById(categoryId);
        model.addAttribute("category", category);
        List<CategoryDto> categories = categoryServices.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories-edit";
    }

    @PostMapping("/{categoryId}/edit")
    public String updateCategory(@PathVariable("categoryId") Long categoryId,
                                 @Valid @ModelAttribute("category") CategoryDto categoryDto,
                                 BindingResult result,
                                 @RequestParam("image") MultipartFile image,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            List<CategoryDto> categories = categoryServices.findAllCategories();
            model.addAttribute("categories", categories);
            return "categories-edit";
        }
        try {
            categoryServices.updateCategory(categoryDto, image);
        } catch (IOException e) {
            e.printStackTrace();
            return "categories-edit";
        }
        return "redirect:/categories/" + categoryId;
    }

    @GetMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId){
        categoryServices.delete(categoryId);
        return "redirect:/menus";
    }
}
