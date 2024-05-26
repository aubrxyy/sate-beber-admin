package com.decode.web.Controller;

import com.decode.web.dto.CategoryDto;
import com.decode.web.services.CategoryServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class CategoryApiController {
    private final CategoryServices categoryServices;

    public CategoryApiController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(value = "/api/categories", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> listMenus(){
        List<CategoryDto> menus = categoryServices.findAllCategories();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }
}