package com.electronic.store.serviceImpl;

import com.electronic.store.Exception.CategoryAlreadyExistException;
import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.CategoryRepository;
import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.entities.Category;
import com.electronic.store.helper.Helper;
import com.electronic.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.image.path}")
    private String FilePath;

    Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto create(CategoryDto categoryDto){
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        //Check if the category already exists
        Category existingCategory = categoryRepository.findByTitle((categoryDto.getTitle()));
        if (existingCategory != null) {
            throw new CategoryAlreadyExistException("Category with title " + categoryDto.getTitle() + " already exists");
        }

        //Create a new category
        Category category= categoryDtoToEntity(categoryDto);
        categoryRepository.save(category);
        CategoryDto categoryDto1 = entityToCategoryDto(category);

        return categoryDto1;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId){

        // Find existing category by title
        Category existingCategory = categoryRepository.findByTitle(categoryDto.getTitle());

        // If title exists but belongs to another category, throw error
        if (existingCategory != null && !existingCategory.getCategoryId().equals(categoryId)) {
            throw new CategoryAlreadyExistException("Category with title " + categoryDto.getTitle() + " already exists");
        }

        // Find category by ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // Update fields
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        // Save and return
        Category updatedCategory = categoryRepository.save(category);
        return entityToCategoryDto(updatedCategory);
    }

    @Override
    public void delete(String categoryId) {
        //Find the category by ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException());
        //delete image
        String imageName=category.getCoverImage();
        String fullPath=FilePath+imageName;

        Path path=Path.of(fullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            logger.error("Failed to delete file: " + fullPath, e);
            throw new ResourceNotFoundException("Failed to delete file: " + fullPath);  // Custom exception
        }
        //Delete the category
        categoryRepository.delete(category);
    }

    @Override
    public PageableRespond<CategoryDto> getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    ){


        // Create a sort object based on the provided sort direction
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
        Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);

        // Create a pageable object with the specified page number and size
        Page<Category> page = categoryRepository.findAll(pageable);

        // Fetch all users from the repository
        List<Category> categories= page.getContent();

        // Convert the list of user entities to a list of user DTOs
        List<CategoryDto> categoryDtos = categories.stream()
                .map(user -> entityToCategoryDto(user))
                .collect(Collectors.toList());

        // Create a pageable response object
        PageableRespond<CategoryDto> respond= Helper.getPageableResponse(page,CategoryDto.class);

        // Return the pageable response
        return respond;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId){
        //Find the category by ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException());
        //Convert the category to DTO
        CategoryDto categoryDto = entityToCategoryDto(category);
        return categoryDto;
    }

    //convert entity to dto
    public CategoryDto entityToCategoryDto(Category category){
        return this.modelMapper.map(category, CategoryDto.class);
    }

    //convert dto to entity
    public Category categoryDtoToEntity(CategoryDto categoryDto){
        return this.modelMapper.map(categoryDto, Category.class);
    }
}
