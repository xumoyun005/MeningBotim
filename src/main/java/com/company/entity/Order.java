package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class Order {
    private Integer id;
    private Customer customer;
    private List<Product> productList;
    private LocalDateTime localDateTime;

}
