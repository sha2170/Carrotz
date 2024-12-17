package com.carrotzmarket.api.domain.productImage.controller;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import com.carrotzmarket.api.domain.productImage.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private static final String UPLOAD_DIRECTORY = "/uploads/images/";

    // 상품 이미지 업로드
    @PostMapping("/upload/{productId}")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        try {
            // 파일 저장 경로 설정
            Path path = Paths.get(UPLOAD_DIRECTORY + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            // 이미지 URL 저장
            String imageUrl = UPLOAD_DIRECTORY + file.getOriginalFilename();
            ProductImageEntity productImage = new ProductImageEntity();
            productImage.setProductId(productId);
            productImage.setImageUrl(imageUrl);

            productImageService.saveProductImage(productImage);

            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
