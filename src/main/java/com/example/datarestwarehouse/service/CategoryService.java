package com.example.datarestwarehouse.service;

import com.example.datarestwarehouse.entity.Category;
import com.example.datarestwarehouse.enums.ElementNotFound;
import com.example.datarestwarehouse.models.CategoryDTO;
import com.example.datarestwarehouse.models.Result;
import com.example.datarestwarehouse.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    ElementNotFound messageCategory = ElementNotFound.CATEGORY;

    @Autowired
    CategoryRepository categoryRepository;

    public Page<Category> getAllCategories(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return categoryRepository.findAll(pageable);
    }

    public Result getCategoryById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.map(category -> new Result(true, category)).orElseGet(() -> new Result(messageCategory.getMessage(), false));
    }

    public List<Result> getCategoriesByParentCategoryId(Integer parent_category_id) {
        List<Result> results = new ArrayList<>();
        boolean exists = categoryRepository.existsByParentCategory_Id(parent_category_id);
        if (exists) {
            List<Category> allByParentCategory_id = categoryRepository.getAllByParentCategory_Id(parent_category_id);
            for (Category category : allByParentCategory_id) {
                Result result = new Result(true, category);
                results.add(result);
            }
            return results;
        }
        Result result = new Result("Bunday id li parent categoriya yo'q", false);
        results.add(result);
        return results;
    }

    private Result addingCategory(CategoryDTO categoryDTO, boolean create, boolean edit, Integer id) {
        Category category = new Category();
        if (create && categoryRepository.existsByName(categoryDTO.getName()) ||
                edit && categoryRepository.existsByIdIsNotAndName(id, categoryDTO.getName())) {
            return new Result("Bunday category bor", false);
        }
        Optional<Category> optionalCategory = categoryRepository.findById(categoryDTO.getCategory_id());
        if (!optionalCategory.isPresent()) {
            return new Result(messageCategory.getMessage(), false);
        }
        Category parent_category = optionalCategory.get();

        category.setName(categoryDTO.getName());
        category.setActive(categoryDTO.isActive());
        category.setParentCategory(parent_category);
        return new Result(true, category);
    }

    public Result addCategory(CategoryDTO categoryDTO) {
        Result result = addingCategory(categoryDTO, true, false, null);
        if (result.isSuccess()) {
            Category category = (Category) result.getObject();
            categoryRepository.save(category);
            return new Result("Category saqlandi", true);
        }
        return result;
    }

    public Result deleteCategoryById(Integer id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.delete(categoryOptional.get());
            return new Result("Category o'chirildi", true);
        }
        return new Result(messageCategory.getMessage(), false);
    }

    public Result editCategoryById(Integer id, CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Result result = addingCategory(categoryDTO, false, true, id);
            if (result.isSuccess()) {
                Category editCategory = optionalCategory.get();
                Category category = (Category) result.getObject();
                editCategory.setParentCategory(category.getParentCategory());
                editCategory.setName(category.getName());
                editCategory.setActive(category.isActive());
                categoryRepository.save(editCategory);
                return new Result("Category o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageCategory.getMessage(), false);
    }
}
