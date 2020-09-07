package com.adison.shop.products;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

    @Bean
    public ProductService productService(ProductRepository productRepository, ProductMapper productMapper) {
        return new ProductService(productRepository, productMapper);
    }
}
