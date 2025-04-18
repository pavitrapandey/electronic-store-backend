package com.electronic.store.service;

import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.ProductDto;

public interface ProductService {

    //Add product
    ProductDto addProduct(ProductDto productDto);
    //Update product
    ProductDto updateProduct(ProductDto productDto, String productId);
    //Get all products
    PageableRespond<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    //Get single product by id
    ProductDto getProductById(String productId);
    //Delete product
    void deleteProduct(String productId);
    //Get all live products
    PageableRespond<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    //Get Product between price range
    PageableRespond<ProductDto> getProductBetweenPrice(long minPrice, long maxPrice, int pageNumber, int pageSize, String sortBy, String sortDir);
    //Delete products between price range
    void deleteProductBetweenPrice(long minPrice, long maxPrice, int pageNumber, int pageSize, String sortBy, String sortDir);
    //Create products by category
    ProductDto createProductByCategory(ProductDto productDto, String categoryId);
    //Update products by category
    ProductDto updateProductByCategory(String productId, String categoryId);
    //Get all products by category
    PageableRespond<ProductDto> getAllProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
