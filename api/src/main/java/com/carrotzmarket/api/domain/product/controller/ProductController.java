package com.carrotzmarket.api.domain.product.controller;


import com.carrotzmarket.api.domain.product.dto.ProductRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.api.domain.product.dto.ProductCreateRequestDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.carrotzmarket.db.product.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * 상품 등록
     * @param productRequestDto 상품 등록 요청 데이터 (상품명, 가격, 설명, 카테고리명 등)
     * @return 등록된 상품 정보
     */

    // 제품 등록
    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductRequestDto productRequestDto) {
        // ProductRequestDto에서 카테고리명과 나머지 정보를 받아서 상품 등록을 처리합니다.
        return productService.createProduct(productRequestDto);
    }

    // 제품 조회
    @GetMapping("/{id}")
    public ProductEntity getProductById(@PathVariable Long id) {
        return productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));

    // 빈 문자열을 null로 변환
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    // 제품 등록
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        Long productId = productService.createProduct(productCreateRequestDto);
        return ResponseEntity.ok("Product created with ID: " + productId);
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

    // 제품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductCreateRequestDto> getProductById(@PathVariable("id") Long id) {
        ProductCreateRequestDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);

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
