package com.carrotzmarket.api.domain.productmanagement.productrepository;

import com.carrotzmarket.api.domain.productmanagement.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public class ProductRepository extends JpaRepository<Product, Long> {

}
