package com.sonnt.blog.controller;

import com.sonnt.blog.payload.CategoryDto;
import com.sonnt.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
//add more information for Controller in swagger
@Tag(
        name = "CRUD REST APIs for Category Controller"
)
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Build Add category rest api
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"//just use name from SecurityScheme in security config
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Build get category rest api
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    //Build get all category rest api
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> list = categoryService.getAllCategories();
        return ResponseEntity.ok(list);
    }

    //Build updated category rest api
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"//just use name from SecurityScheme in security config
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable(value = "id") Long categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, categoryId));
    }

    //Build delete category rest api
    // add require for jwt authentication in swagger
    @SecurityRequirement(
            name = "Bear Authentication"//just use name from SecurityScheme in security config
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successful with id: " + categoryId);
    }


}
