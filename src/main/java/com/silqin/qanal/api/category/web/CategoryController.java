package com.silqin.qanal.api.category.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silqin.qanal.api.category.service.CategoryService;
import com.silqin.qanal.core.domain.Category;
import com.silqin.qanal.core.exception.ResourceNotFoundException;
import com.silqin.qanal.core.util.ApiResponse;


@RequestMapping("/category")
@RestController
public class CategoryController {
    
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> list() {
        try {
            
            List<Category> list = categoryService.getList();
            ApiResponse<List<Category>> response = ApiResponse.success(list);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
            
        }
    }

}
