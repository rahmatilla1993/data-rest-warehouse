package com.example.datarestwarehouse.controller;

import com.example.datarestwarehouse.entity.Category;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.CategoryDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    ElementNotFound messageCategory = ElementNotFound.CATEGORY;

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public HttpEntity<?> getAllCategory(@RequestParam Integer page) {
        Page<Category> allCategories = categoryService.getAllCategories(page);
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getCategoryById(@PathVariable Integer id) {
        Result result = categoryService.getCategoryById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byParentCategoryId/{parent_category_id}")
    public HttpEntity<?> getCategoriesByParentCategoryId(@PathVariable Integer parent_category_id) {
        List<Result> results = categoryService.getCategoriesByParentCategoryId(parent_category_id);
        return ResponseEntity.status(results.get(0).isSuccess() ? HttpStatus.NO_CONTENT :
                HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Result result = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteCategoryById(@PathVariable Integer id) {
        Result result = categoryService.deleteCategoryById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editCategoryById(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        Result result = categoryService.editCategoryById(id, categoryDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageCategory.getMessage()) ?
                HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }
}
