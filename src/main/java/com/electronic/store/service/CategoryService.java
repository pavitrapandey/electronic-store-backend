package com.electronic.store.service;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableRespond;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;

import java.util.List;

public interface CategoryService {

    // create
    CategoryDto create(CategoryDto categoryDto);

    // update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

    // delete
    void delete(String categoryId);

    // get all
    PageableRespond<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // get single
    CategoryDto getCategoryById(String categoryId);
}
