package com.company.service;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.entity.CartProduct;
import com.company.entity.Customer;
import com.company.entity.Product;
import com.company.load.WorkWithFiles;

import java.util.Optional;

public class CartProductService {

    public static void add(String userId, Integer productId, Integer quantity){
        Customer customer = CustomerService.getCustomerById(userId);
        Product product = ProductService.getProductById(productId);

        Optional<CartProduct> optional = Database.CART_PRODUCTS.stream()
                .filter(cartProduct -> {
                    if (!cartProduct.getCustomer().getUserId().equals(customer.getUserId())) return false;
                    assert product != null;
                    return cartProduct.getProduct().getId().equals(product.getId());
                })
                .findFirst();

        if(optional.isPresent()){
            CartProduct cartProduct = optional.get();
            cartProduct.setQuantity(cartProduct.getQuantity()+quantity);
        }else{
            CartProduct cartProduct = new CartProduct(++ComponentContainer.generalId, customer, product, quantity);
            Database.CART_PRODUCTS.add(cartProduct);
        }

        WorkWithFiles.writeToJson(Database.CART_PRODUCTS, ComponentContainer.CART_PRODUCTS_FILE);
    }


    public static void delete(Integer cartProductId) {
        Database.CART_PRODUCTS.removeIf(cartProduct -> cartProduct.getId().equals(cartProductId));
        WorkWithFiles.writeToJson(Database.CART_PRODUCTS, ComponentContainer.CART_PRODUCTS_FILE);
    }

    public static void delete(String userId) {
        Database.CART_PRODUCTS.removeIf(cartProduct -> cartProduct.getCustomer().getUserId().equals(userId));
        WorkWithFiles.writeToJson(Database.CART_PRODUCTS, ComponentContainer.CART_PRODUCTS_FILE);
    }
}
