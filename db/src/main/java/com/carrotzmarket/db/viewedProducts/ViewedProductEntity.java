package com.carrotzmarket.db.viewedProducts;

import com.carrotzmarket.db.product.ProductEntity;
import com.carrotzmarket.db.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "viewed_products")
@Data
@NoArgsConstructor
public class ViewedProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;
}
