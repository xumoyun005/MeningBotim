package com.company.service;

import com.company.db.Database;
import com.company.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    public static List<Product> getProductsByCategoryId(Integer categoryId){
        return Database.PRODUCTS.stream()
                .filter(product -> product.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public static Product getProductById(Integer productId){
        Optional<Product> optional = Database.PRODUCTS.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
        return optional.orElse(null);
    }

}
