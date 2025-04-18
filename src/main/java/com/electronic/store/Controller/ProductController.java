package com.electronic.store.Controller;

import com.electronic.store.dtos.ImageResponse;
import com.electronic.store.dtos.PageableRespond;
import com.electronic.store.dtos.ProductDto;
import com.electronic.store.dtos.UserDto;
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
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    public ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imageUploadPath;

    //create product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto){
        ProductDto createdProduct = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    //update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto, @PathVariable String productId){
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    //get all products
    @GetMapping
    public ResponseEntity<PageableRespond<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableRespond<ProductDto> products = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //get single product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
        ProductDto product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }

    //get all live products
    @GetMapping("/live")
    public ResponseEntity<PageableRespond<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
       PageableRespond<ProductDto> live=productService.getAllLiveProducts(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(live,HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<PageableRespond<ProductDto>> getProductsByPriceRange(
            @RequestParam(value = "min") long minPrice,
            @RequestParam(value = "max") long maxPrice,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageableRespond<ProductDto> products = productService.getProductBetweenPrice(minPrice, maxPrice, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/price-range")
    public ResponseEntity<String> DeleteProductsByPriceRange(
            @RequestParam(value = "min") long minPrice,
            @RequestParam(value = "max") long maxPrice,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        productService.deleteProductBetweenPrice(minPrice, maxPrice, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>("Deleted the product between price "+minPrice+" to "+maxPrice, HttpStatus.OK);
    }

    //Upload product image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @PathVariable String productId,
            @RequestParam("image") MultipartFile image) throws IOException {

        String imageName=  fileService.uploadImage(image,imageUploadPath);
       ProductDto productDto= productService.getProductById(productId);
        productDto.setProductImageName(imageName);
        ProductDto updated= productService.updateProduct(productDto,productId);

        ImageResponse imageResponse=  ImageResponse.builder().
                imageName(imageName).
                success(true).
                message("Image Uploaded").
                status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDto product = productService.getProductById(productId);
        InputStream resource = fileService.getResource(imageUploadPath, product.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
