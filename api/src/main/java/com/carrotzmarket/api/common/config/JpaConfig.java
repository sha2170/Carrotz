package com.carrotzmarket.api.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.carrotzmarket.db")
@EnableJpaRepositories(basePackages = {"com.carrotzmarket.db", "com.carrotzmarket.api"})
public class JpaConfig {
}
