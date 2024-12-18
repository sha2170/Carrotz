package com.carrotzmarket.api.domain.product.controller;

import com.carrotzmarket.api.domain.product.dto.ProductDto;
import com.carrotzmarket.api.domain.product.service.ProductService;
import com.carrotzmarket.db.product.ProductEntity;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.carrotzmarket.db.product.ProductStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 제품 등록
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDto productDto) {
        Long productId = productService.createProduct(productDto);
        return ResponseEntity.ok("Product created with ID: " + productId);
    }

    // 제품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        ProductDto product = productService.getProductById(id);
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

    /*// 특정 카테고리로 검색
    @GetMapping("/category/detail")
    public List<ProductEntity> getProductByDetailCategoryId(@RequestParam Long detailCategoryId) {
        return productService.getProductByCategory(detailCategoryId);
    }
*/
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
}
