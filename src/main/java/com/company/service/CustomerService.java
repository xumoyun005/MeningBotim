package com.company.service;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.entity.CartProduct;
import com.company.entity.Customer;
import com.company.enums.CustomerStatus;
import com.company.enums.Language;
import com.company.load.WorkWithFiles;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerService {

    public static void addCustomer(String userId){
        Customer customer = getCustomerById(userId);
        if(customer == null){
            customer = new Customer();
            customer.setUserId(userId);
            customer.setStatus(CustomerStatus.SHARE_CONTACT);

            Database.CUSTOMERS.add(customer);
            WorkWithFiles.writeToJson(Database.CUSTOMERS, ComponentContainer.CUSTOMERS_FILE);
        }
    }

    public static Customer getCustomerById(String userId){

        Optional<Customer> optional = Database.CUSTOMERS.stream()
                .filter(customer -> customer.getUserId().equals(userId))
                .findFirst();


        return optional.orElse(null);
    }

    public static void setContactToCustomer(Contact contact) {
        String userId = String.valueOf(contact.getUserId());

        Customer customer = getCustomerById(userId);
        if(customer != null){
            customer.setFirstName(contact.getFirstName());
            customer.setLastName(contact.getLastName());
            customer.setPhoneNumber(contact.getPhoneNumber());
            customer.setStatus(CustomerStatus.SELECT_MENU);

            WorkWithFiles.writeToJson(Database.CUSTOMERS, ComponentContainer.CUSTOMERS_FILE);
        }
    }

    public static Language getLanguageByUserId(String userId){
        Customer customer = getCustomerById(userId);
        return Objects.requireNonNull(customer).getLanguage();
    }

    public static List<CartProduct> getCartProducts(String userId){
        return Database.CART_PRODUCTS.stream()
                .filter(cartProduct -> cartProduct.getCustomer().getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
