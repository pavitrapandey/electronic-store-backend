package com.electronic.store.Controller;

import com.electronic.store.dtos.*;
import com.electronic.store.service.CategoryService;
import com.electronic.store.service.FileService;
import com.electronic.store.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;

    @Value("${category.image.path}")
    private String UploadPath;

    //create category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto){
        CategoryDto createdCategory = categoryService.create(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    //update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            @PathVariable String categoryId) {

        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    //Get category by id
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        CategoryDto category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //get all Category
    @GetMapping
    public ResponseEntity<PageableRespond<CategoryDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "size",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    //delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

    //Upload category image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable String categoryId
    ) throws IOException {
        String imageName=  fileService.uploadImage(image,UploadPath);
       CategoryDto categoryDto= categoryService.getCategoryById(categoryId);
       categoryDto.setCoverImage(imageName);
        CategoryDto updated= categoryService.update(categoryDto,categoryId);

        ImageResponse imageResponse=  ImageResponse.builder().imageName(imageName).success(true).message("Image Uploaded").status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    //serve category image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        InputStream resource = fileService.getResource(UploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody  ProductDto productDto
    )
    {
        ProductDto createdProduct = productService.createProductByCategory(productDto, categoryId);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductByCategory(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    )
    {
        ProductDto updatedProduct = productService.updateProductByCategory(productId, categoryId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableRespond<ProductDto>> getAllProductsByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "size",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    )
    {
        PageableRespond<ProductDto> productsByCategory = productService.getAllProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }
}
