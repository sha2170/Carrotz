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

    // 제품 등록
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity product = productService.createProduct(productCreateRequestDto);
        return ResponseEntity.ok("Product created with ID: " + product);
    }

    // 제품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    // 제품 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequestDto updateRequest) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, updateRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    // 제품 삭제
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }


    // 거래 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponseDto> updateProductStatus(
            @PathVariable Long id,
            @RequestParam ProductStatus status) {
        ProductResponseDto updatedProduct = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(updatedProduct);
    }



    // 유효성 검사 실패 시 오류 응답 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 각 필드별 오류 메시지 추출
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 특정 사용자 제품 조회
    @GetMapping("/user/{userId}")
    public List<ProductEntity> getProductByUserId(@PathVariable Long userId) {
        return productService.getProductByUserId(userId);
    }

    // 제품 이름으로 검색
    @GetMapping("/search")
    public List<ProductEntity> searchProducts(@RequestParam String title) {
        return productService.searchProductByTitle(title);
    }

    // 제품 상태로 검색
    @GetMapping("/status")
    public List<ProductEntity> getProductsByStatus(@RequestParam ProductStatus status) {
        return productService.getProductByStatus(status);
    }

    // 특정 지역으로 검색
    @GetMapping("/region")
    public List<ProductEntity> getProductByRegion(@RequestParam Long regionId) {
        return productService.getProductByRegion(regionId);
    }

    // 최근 올라온 제품 10개 조회
    @GetMapping("/top")
    public List<ProductEntity> getTop10Product() {
        return productService.getTop10Products();
    }

    // 특정 사용자와 상태 기준으로 검색
    @GetMapping("/user/status")
    public List<ProductEntity> getProductByUserIdAndStatus(@RequestParam Long userId, @RequestParam ProductStatus status) {
        return productService.getProductByUserIdAndStatus(userId, status);
    }

    // 특정 카테고리로 검색
    @GetMapping("/category")
    public List<ProductEntity> getProductByCategory(@RequestParam String categoryName) {
        return productService.getProductByCategory(categoryName);
    }

}
