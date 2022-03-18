package com.company.load;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.entity.CartProduct;
import com.company.entity.Category;
import com.company.entity.Customer;
import com.company.entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class WorkWithFiles {

    public static void writeToJson(List list, File file){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(list);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void readCategories(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new BufferedReader(new FileReader(ComponentContainer.CATEGORIES_FILE))) {
            Type type = new TypeToken<List<Category>>(){}.getType();

            List<Category> list = gson.fromJson(reader, type);
            Database.CATEGORIES.clear();
            Database.CATEGORIES.addAll(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readProducts(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new BufferedReader(new FileReader(ComponentContainer.PRODUCTS_FILE))) {
            Type type = new TypeToken<List<Product>>(){}.getType();

            List<Product> list = gson.fromJson(reader, type);
            Database.PRODUCTS.clear();
            Database.PRODUCTS.addAll(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readCustomers(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new BufferedReader(new FileReader(ComponentContainer.CUSTOMERS_FILE))) {
            Type type = new TypeToken<List<Customer>>(){}.getType();

            List<Customer> list = gson.fromJson(reader, type);
            Database.CUSTOMERS.clear();
            Database.CUSTOMERS.addAll(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readCartProducts(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new BufferedReader(new FileReader(ComponentContainer.CART_PRODUCTS_FILE))) {
            Type type = new TypeToken<List<CartProduct>>(){}.getType();

            List<CartProduct> list = gson.fromJson(reader, type);
            Database.CART_PRODUCTS.clear();
            Database.CART_PRODUCTS.addAll(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
