package com.carrotzmarket.api.domain.product.controller;

import com.carrotzmarket.api.domain.category.dto.CategoryDto;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.dto.ProductResponseDto;
import com.carrotzmarket.api.domain.product.dto.ProductUpdateRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.product.ProductStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String addFavoriteProduct(@RequestParam Long userId, @PathVariable Long productId) {
        try {
            return productService.addFavoriteProduct(userId, productId);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }


    @DeleteMapping("/{productId}/favorite")
    public ResponseEntity<String> removeFavoriteProduct(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        String message = productService.removeFavoriteProduct(userId, productId);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductResponseDto>> getProductByUserId(@PathVariable Long userId) {
        List<ProductEntity> products = productService.getProductByUserId(userId);

        List<ProductResponseDto> response = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{userId}/favorites")
    public ResponseEntity<?> getFavoriteProducts(@PathVariable Long userId) {
        List<Object> favoriteProducts = productService.getFavoriteProductsByUserId(userId);

        if (favoriteProducts.size() == 1 && favoriteProducts.get(0) instanceof String) {
            return ResponseEntity.ok(Map.of("message", favoriteProducts.get(0)));
        }

        return ResponseEntity.ok(favoriteProducts);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("해당 상품이 삭제되었습니다.");
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


    @GetMapping("/category-name")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryName(@RequestParam String categoryName) {
        List<ProductEntity> products = productService.getProductsByCategoryName(categoryName);

        List<ProductResponseDto> response = products.stream()
                .map(ProductResponseDto::new)
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


    @GetMapping("/user/status")
    public List<ProductEntity> getProductByUserIdAndStatus(@RequestParam Long userId, @RequestParam ProductStatus status) {
        return productService.getProductByUserIdAndStatus(userId, status);
    }
}
