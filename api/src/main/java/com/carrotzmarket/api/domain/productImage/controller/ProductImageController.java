package com.carrotzmarket.api.domain.productImage.controller;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import com.carrotzmarket.api.domain.productImage.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/products/images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    // 디렉터리 설정 옵션 : 절대 경로
    private static final String UPLOAD_DIRECTORY = "/Users/tjdgusdk/uploads/";

    // 상품 이미지 업로드
    @PostMapping(value = "/upload/{productId}", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            // 파일명 생성
            String safeFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            // 파일 저장 경로 설정
            Path uploadDir = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // 디렉터리가 없으면 생성
            }

            Path filePath = uploadDir.resolve(safeFileName);
            Files.write(filePath, file.getBytes());

            // 이미지 URL 저장
            String imageUrl = UPLOAD_DIRECTORY + safeFileName;
            ProductImageEntity productImage = new ProductImageEntity();
            productImage.setProductId(productId);
            productImage.setImageUrl(imageUrl);

            productImageService.saveProductImage(productImage);

            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 상품 이미지 조회
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductImageByProductId(@PathVariable Long productId) {
        ProductImageEntity image = productImageService.getProductImageByProductId(productId);

        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.status(404).body("Image not found for productId: " + productId);
        }
    }
}
