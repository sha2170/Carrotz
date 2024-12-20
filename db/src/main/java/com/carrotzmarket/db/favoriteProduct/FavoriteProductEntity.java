package com.carrotzmarket.db.favoriteProduct;

import com.carrotzmarket.db.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FavoriteProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;


}