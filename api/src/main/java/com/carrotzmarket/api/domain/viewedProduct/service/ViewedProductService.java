package com.carrotzmarket.api.domain.viewedProduct.service;

import com.carrotzmarket.api.domain.viewedProduct.repository.ViewedProductRepository;
import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.user.UserEntity;
import com.carrotzmarket.db.viewedProducts.ViewedProductEntity;
import com.carrotzmarket.api.domain.user.repository.UserRepository;
import com.carrotzmarket.api.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ViewedProductService {

    private final ViewedProductRepository viewedProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void recordViewedProduct(Long userId, Long productId) {
        Optional<ViewedProductEntity> existingRecord = viewedProductRepository.findByUserIdAndProductId(userId, productId);

        if (existingRecord.isEmpty()) {
            ViewedProductEntity viewedProduct = new ViewedProductEntity();
            viewedProduct.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
            viewedProduct.setProduct(productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found")));
            viewedProduct.setViewedAt(LocalDateTime.now());
            viewedProductRepository.save(viewedProduct);
        }
    }

    public Set<Long> getViewedProductIds(Long userId) {
        return viewedProductRepository.findByUserId(userId).stream()
                .map(viewedProduct -> viewedProduct.getProduct().getId())
                .collect(Collectors.toSet());
    }

    public List<ViewedProductEntity> getViewedProductsByUser(Long userId) {
        return viewedProductRepository.findByUserId(userId);
    }
}
