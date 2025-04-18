package com.electronic.store.service.impl;

import com.electronic.store.Exception.ResourceNotFoundException;
import com.electronic.store.Repository.CategoryRepository;
import com.electronic.store.Repository.ProductRepository;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.helper.Helper;
import com.electronic.store.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${product.image.path}")
    private String path;

    Logger logger= org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto addProduct(ProductDto productDto){
        // Generate a unique product ID
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        //Set current date and time
        Date date = new Date();
        productDto.setAddedDate(date);
        // Convert ProductDto to Product entity
        Product p1= modelMapper.map(productDto, Product.class);
        // Save the product entity to the database
        Product saved=productRepository.save(p1);
        // Convert the saved product entity back to DTO
        ProductDto p2= modelMapper.map(saved, ProductDto.class);
        return p2;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId){
        // Find the existing product by ID
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        // Update the product details
        existingProduct.setTitle(productDto.getTitle());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setProductImageName(productDto.getProductImageName());
        existingProduct.setLive(productDto.isLive());
        // Set the current date and time
        Date date = new Date();
        existingProduct.setAddedDate(date);
        // Save the updated product entity to the database
        Product updatedProduct = productRepository.save(existingProduct);
        // Convert the updated product entity back to DTO
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }

    @Override
    public PageableRespond<ProductDto> getAllProducts
            (
                    int pageNumber,
                    int pageSize,
                    String sortBy,
                    String sortDir
            ){
        // Sort the products based on the provided sortBy and sortDir
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch the products from the repository with pagination and sorting
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        //Fetch all products from the repository
        List<Product> products= page.getContent();
        // Convert the list of product entities to a list of product DTOs
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        // Create a pageable response object
        PageableRespond<ProductDto> respond=  Helper.getPageableResponse(page, ProductDto.class);
        return respond;
    }

    @Override
    public ProductDto getProductById(String productId){
        // Find the product by ID
      Product product=  productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found with id: " + productId));
       // Convert the product entity to DTO
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        // Return the product DTO
        return productDto;
    }

    @Override
    public void deleteProduct(String productId) {

        //Find the Product by ID
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        //delete image
        String imageName = product.getProductImageName();
        String fullPath = path + imageName;

        Path path = Path.of(fullPath);
        try {
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Image deleted successfully: " + fullPath);
            } else {
                logger.warn("Image file does not exist: " + fullPath);
            }
        } catch (IOException e) {
            logger.error("Could not delete image file: " + fullPath + ". Reason: " + e.getMessage());
            // Exception ko ignore karke aage proceed karein
        }

        // Delete the product from the repository
        productRepository.delete(product);
    }


    @Override
    public PageableRespond<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Sort the products based on the provided sortBy and sortDir
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch the products from the repository with pagination and sorting
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        //Fetch all products from the repository
        Page<Product> liveProducts = productRepository.findByLiveTrue(pageable);
        List<ProductDto> productDto= liveProducts.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        PageableRespond<ProductDto> respond=  Helper.getPageableResponse(page, ProductDto.class);
        return respond;
    }

    @Override
    public PageableRespond<ProductDto> getProductBetweenPrice(long minPrice, long maxPrice, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Sort the products based on the provided sortBy and sortDir
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch the products from the repository with pagination and sorting
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByPriceBetween(minPrice,maxPrice,pageable);
        //Fetch products between min and max from the repository
        Page<Product> products = productRepository.findByPriceBetween(minPrice,maxPrice,pageable);
        List<ProductDto> productDto= products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        PageableRespond<ProductDto> respond=  Helper.getPageableResponse(page, ProductDto.class);
        return respond;
    }

    @Override
    public void deleteProductBetweenPrice(long minPrice, long maxPrice, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Sort the products based on the provided sortBy and sortDir
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Fetch products between min and max price
        Page<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

        for (Product product : products) {
            // Delete the product image from the file system
            String imageName = product.getProductImageName();
            if (imageName != null && !imageName.isBlank()) {
                String fullPath = path + File.separator + imageName; // Use your configured path
                Path paths = Path.of(fullPath);
                try {
                    if (Files.exists(paths)) {
                        Files.delete(paths);
                        logger.info("Image deleted successfully: " + fullPath);
                    } else {
                        logger.warn("Image file does not exist: " + fullPath);
                    }
                } catch (IOException e) {
                    logger.error("Could not delete image file: " + fullPath + ". Reason: " + e.getMessage());
                    // Exception ko ignore karke aage proceed karein
                }
            }

            // Delete the product from the repository
            productRepository.delete(product);
        }
    }

    @Override
    public ProductDto createProductByCategory(ProductDto productDto, String categoryId){
        // Find the category by ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        //Set current date and time
        Date date = new Date();
        productDto.setAddedDate(date);
        // Convert ProductDto to Product entity
        Product p1= modelMapper.map(productDto, Product.class);
        // Set the category for the product
        p1.setCategory(category);
        // Save the product entity to the database
        Product saved=productRepository.save(p1);
        // Convert the saved product entity back to DTO
        ProductDto p2= modelMapper.map(saved, ProductDto.class);
        return p2;

    }

    @Override
    public ProductDto updateProductByCategory(String productId, String categoryId) {
        Product product= productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        // Find the category by ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        // Update the product details
        product.setCategory(category);
        // Save the updated product entity to the database
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public PageableRespond<ProductDto> getAllProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Sort the products based on the provided sortBy and sortDir
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch the products from the repository with pagination and sorting
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Category category= categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        Page<Product> page = productRepository.findByCategory(category,pageable);
        //Fetch all products from the repository
        Page<Product> products = productRepository.findByCategory(category,pageable);
        List<ProductDto> productDto=products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        PageableRespond<ProductDto> respond=  Helper.getPageableResponse(page, ProductDto.class);
        return respond;
    }

}
