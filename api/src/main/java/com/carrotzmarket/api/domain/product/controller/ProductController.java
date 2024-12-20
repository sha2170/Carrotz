package com.carrotzmarket.api.domain.product.controller;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.carrotzmarket.db.product.ProductStatus;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;


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

    @PostMapping("/{productId}/favorite")
    public ResponseEntity<String> addFavoriteProduct(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        try {
            String message = productService.addFavoriteProduct(userId, productId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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

    @DeleteMapping("/{productId}/favorite")
    public ResponseEntity<String> removeFavoriteProduct(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        try {
            String message = productService.removeFavoriteProduct(userId, productId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
    public ResponseEntity<List<ProductResponseDto>> getProductByUserId(@PathVariable Long userId) {
        List<ProductEntity> products = productService.getProductByUserId(userId);
        List<ProductResponseDto> response = products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getUserId(),
                        product.getRegionId(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()),  // CategoryDto로 변경
                        product.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/favorites")
    public ResponseEntity<?> getFavoriteProducts(@PathVariable Long userId) {
        List<ProductResponseDto> favoriteProducts = productService.getFavoriteProductsByUserId(userId);

        if (favoriteProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관심 상품으로 등록한 상품이 없습니다.");
        }
        return ResponseEntity.ok(favoriteProducts);
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

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductEntity> products = productService.getProductsByCategory(categoryId);
        List<ProductResponseDto> response = products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getUserId(),
                        product.getRegionId(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()),
                        product.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-name")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryName(@RequestParam String categoryName) {
        List<ProductEntity> products = productService.getProductsByCategoryName(categoryName);
        List<ProductResponseDto> response = products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getUserId(),
                        product.getRegionId(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription(), product.getCategory().isEnabled()),
                        product.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/sorted-by-created-at-updated-at")
    public List<ProductEntity> getProductsSortedByCreatedAtAndUpdatedAt() {
        return productService.getProductsSortedByCreatedAtAndUpdatedAt();
    }

    @GetMapping("/sorted-by-created-at")
    public List<ProductEntity> getProductsSortedByCreatedAt() {
        return productService.getProductsSortedByCreatedAt();
    }

    @GetMapping("/sorted-by-updated-at")
    public List<ProductEntity> getProductsSortedByUpdatedAt() {
        return productService.getProductsSortedByUpdatedAt();
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam(defaultValue = "0") int minPrice,
            @Parameter(
                    schema = @Schema(allowableValues = {"0","5000", "10000", "20000"}),
                    in = ParameterIn.QUERY
            )
            @RequestParam int maxPrice) {

        List<ProductEntity> products = productService.getProductsByPriceRangeAndSort(minPrice, maxPrice);

        List<ProductResponseDto> response = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCustomPriceRange(
            @PathVariable int minPrice,
            @PathVariable int maxPrice) {
        List<ProductEntity> products = productService.getProductsByPriceRangeAndSort(minPrice, maxPrice);
        List<ProductResponseDto> response = products.stream()
                .map(product -> new ProductResponseDto(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}