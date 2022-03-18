package com.company.controller;

import com.company.entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class FileControllerExcel {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String PATH = "src/main/resources/";
    public static File generateCustomersPDF(){

            File file = new File(PATH + "g4.xlsx");
            try (FileOutputStream out = new FileOutputStream(file)) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheets = workbook.createSheet("Products name");

                for (int i = 0; i < 5; i++) {
                    sheets.autoSizeColumn(i);
                }


                XSSFRow run = sheets.createRow(0);
                run.createCell(0).setCellValue("id");
                run.createCell(1).setCellValue("Category");
                run.createCell(2).setCellValue("Name Uz");
                run.createCell(3).setCellValue("Name Ru");
                run.createCell(4).setCellValue("Price");
                run.createCell(5).setCellValue("Image URL");


                List<Product> sheets1 = getComments();

                for (int i = 0; i < Objects.requireNonNull(sheets1).size(); i++) {
                    Product comment = sheets1.get(i);

                    XSSFRow row = sheets.createRow(i + 1);
                    row.createCell(0).setCellValue(comment.getId());
                    row.createCell(1).setCellValue(comment.getCategory().toString());
                    row.createCell(2).setCellValue(comment.getNameUz());
                    row.createCell(3).setCellValue(comment.getNameRu());
                    row.createCell(4).setCellValue(comment.getPrice());
                    row.createCell(5).setCellValue(comment.getImageUrl());
                }
                for (int i = 0; i < 6; i++ ){
                    sheets.autoSizeColumn(i);
                }
                workbook.write(out);
                workbook.close();
                return file;

            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static List<Product> getComments(){
        String path = PATH + "files/db/products.json";

        try (Reader reader = new BufferedReader(new FileReader(path))) {

            Type type = new TypeToken<List<Product>>() {
            }.getType();

            return gson.fromJson(reader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
