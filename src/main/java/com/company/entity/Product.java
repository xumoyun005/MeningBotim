package com.company.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private Category category;
    private String nameUz;
    private String nameRu;
    private double price;
    private String imageUrl;
}
