package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.entity.Customer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;



import java.io.File;
import java.io.IOException;

public class FileController {
    public static File generateCustomersPDF(){

        File file = new File(ComponentContainer.PATH + "files/documents/customers.pdf");

        try (PdfWriter pdfWriter = new PdfWriter(file)) {

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph();
            paragraph.add("CUSTOMERS");

            document.add(paragraph);

            float[] widthColumns = {200f, 200f};
            Table table = new Table(widthColumns);
            table.addCell("ID");
            table.addCell("FIRST NAME");
            document.add(table);

            for (Customer customer : Database.CUSTOMERS) {
                Table table1 = new Table(widthColumns);
                table1.addCell(customer.getUserId());
                table1.addCell(customer.getFirstName()==null ? "" :customer.getFirstName());
                document.add(table1);
            }
            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
