package com.silqin.qanal.api.category.service;

import org.springframework.stereotype.Service;

import com.silqin.qanal.core.domain.Category;
import com.silqin.qanal.core.mapper.CategoryMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getList() {
        return categoryMapper.getList();
    }

    public Category getCategory(String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    public void insert(Category category) {
        categoryMapper.insert(category);
    }

    public void update(Category category) {
        categoryMapper.update(category);
    }

    public void delete(String categoryId) {
        categoryMapper.delete(categoryId);
    }

    public void deleteAll() {
        categoryMapper.deleteAll();
    }

}
