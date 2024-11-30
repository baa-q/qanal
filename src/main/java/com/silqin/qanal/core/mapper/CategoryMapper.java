package com.silqin.qanal.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.silqin.qanal.core.domain.Category;

@Mapper  // @Mapper 어노테이션을 추가합니다.
public interface CategoryMapper {
    
    List<Category> getList();

    Category getCategory(String categoryId);

    void insert(Category category);

    void update(Category category);

    void delete(String categoryId);

    void deleteAll();
}
