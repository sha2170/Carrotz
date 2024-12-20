package com.carrotzmarket.api.domain.product.controller;

import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.carrotzmarket.db.product.ProductStatus;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createProduct(@ModelAttribute ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity product = productService.createProduct(productCreateRequestDto);
        return ResponseEntity.ok("Product created with ID: " + product.getId());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductResponseDto response = productService.getProductById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequestDto updateRequest) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, updateRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponseDto> updateProductStatus(@PathVariable Long id, @RequestParam ProductStatus status) {
        ProductResponseDto updatedProduct = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(updatedProduct);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @GetMapping("/user/{userId}")
    public List<ProductEntity> getProductByUserId(@PathVariable Long userId) {
        return productService.getProductByUserId(userId);
    }

    @GetMapping("/search")
    public List<ProductEntity> searchProducts(@RequestParam String title) {
        return productService.searchProductByTitle(title);
    }

    @GetMapping("/status")
    public List<ProductEntity> getProductsByStatus(@RequestParam ProductStatus status) {
        return productService.getProductByStatus(status);
    }

    @GetMapping("/region")
    public List<ProductEntity> getProductByRegion(@RequestParam Long regionId) {
        return productService.getProductByRegion(regionId);
    }

    @GetMapping("/top")
    public List<ProductEntity> getTop10Product() {
        return productService.getTop10Products();
    }

    @GetMapping("/user/status")
    public List<ProductEntity> getProductByUserIdAndStatus(@RequestParam Long userId, @RequestParam ProductStatus status) {
        return productService.getProductByUserIdAndStatus(userId, status);
    }

    @GetMapping("/category")
    public List<ProductEntity> getProductByCategory(@RequestParam String categoryName) {
        return productService.getProductByCategory(categoryName);
    }

    @GetMapping
    public List<ProductEntity> getFilteredProducts(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
    ) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    // 정렬 범위로 상품 필터링
    @GetMapping("/sort")
    public List<ProductEntity> getSortedProducts(
            @RequestParam String sortBy,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
    ) {
        return productService.getProductsSortedBy(sortBy, minPrice, maxPrice);
    }

    // 카테고리 범위로 상품 필터링
    @GetMapping("/category/filter")
    public List<ProductEntity> getProductsByCategory(
            @RequestParam Long categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        return productService.getProductsByCategory(categoryId, minPrice, maxPrice, sortBy);
    }

    // 지역 기반 상품 필터링
    @GetMapping("/region/filter")
    public List<ProductEntity> getProductsByRegion(
            @RequestParam Long regionId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        return productService.getProductsByRegion(regionId, minPrice, maxPrice, sortBy);
    }
}