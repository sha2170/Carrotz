package com.carrotzmarket.api.domain.product.controller;

import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createProduct(@ModelAttribute @Valid ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity product = productService.createProduct(productCreateRequestDto);
        return ResponseEntity.ok("Product created with ID: " + product.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequestDto updateRequest) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, updateRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponseDto> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        ProductResponseDto updatedProduct = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/{productId}/favorite")
    public ResponseEntity<String> addFavoriteProduct(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        String message = productService.addFavoriteProduct(userId, productId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{productId}/favorite")
    public ResponseEntity<String> removeFavoriteProduct(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        String message = productService.removeFavoriteProduct(userId, productId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/user/{userId}/favorites")
    public ResponseEntity<List<ProductResponseDto>> getFavoriteProductsByUserId(@PathVariable Long userId) {
        List<ProductResponseDto> favorites = productService.getFavoriteProductsByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductEntity>> searchProductsByTitle(@RequestParam String title) {
        List<ProductEntity> products = productService.searchProductByTitle(title);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductEntity>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductEntity> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<ProductEntity>> getProductsByRegion(@PathVariable Long regionId) {
        List<ProductEntity> products = productService.getProductByRegion(regionId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/status")
    public ResponseEntity<List<ProductEntity>> getProductsByStatus(@RequestParam ProductStatus status) {
        List<ProductEntity> products = productService.getProductByStatus(status);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/top")
    public ResponseEntity<List<ProductEntity>> getTop10Products() {
        List<ProductEntity> products = productService.getTop10Products();
        return ResponseEntity.ok(products);
    }
}
