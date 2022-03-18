package com.company.db;

import com.company.entity.CartProduct;
import com.company.entity.Category;
import com.company.entity.Customer;
import com.company.entity.Product;

import java.util.LinkedList;
import java.util.List;

public interface Database {
    List<Customer> CUSTOMERS = new LinkedList<>();
    List<Category> CATEGORIES = new LinkedList<>();
    List<Product> PRODUCTS = new LinkedList<>();
    List<CartProduct> CART_PRODUCTS = new LinkedList<>();
}
