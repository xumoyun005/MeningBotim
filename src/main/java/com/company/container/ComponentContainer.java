package com.company.container;

import com.company.MyTelegramBot;

import java.io.File;

public class ComponentContainer {

    public static MyTelegramBot MY_TELEGRAM_BOT;

  public static final String ADMIN = "1879817666";

    public static int generalId = 0;

    public static String PATH = "src/main/resources/";

    public static final File CATEGORIES_FILE = new File(ComponentContainer.PATH+"files/db/categories.json");

    public static final File PRODUCTS_FILE = new File(ComponentContainer.PATH+"files/db/products.json");

    public static final File CUSTOMERS_FILE = new File(ComponentContainer.PATH+"files/db/customers.json");

    public static final File CART_PRODUCTS_FILE = new File(ComponentContainer.PATH+"files/db/cartproducts.json");

}