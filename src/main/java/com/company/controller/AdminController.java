package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.entity.Customer;
import com.company.enums.Language;
import com.company.util.DemoUtil;
import com.company.util.InlineKeyboardButtonUtil;
import com.company.util.KeyboardButtonUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


import static com.company.container.ComponentContainer.*;

public class AdminController extends Thread {

    private static Message message;

    public AdminController(Message message) {
        AdminController.message = message;
    }

    public static void workWithCallbackQuery() {
    }

    @Override
    public void run() {
        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                Language language = Language.UZ;
                SendMessage sendMessage = new SendMessage(ADMIN,
                        "Assalomu alaykum ADMIN!" +
                                "\nNima ish bajarmoqchisiz tanlang:");
                ReplyKeyboardMarkup adminKeyboard = KeyboardButtonUtil.getAdminKeyboard(language);
                sendMessage.setReplyMarkup(adminKeyboard);

                MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }
        if (message.getText().equals("Barcha foydalanuvchilar ro'yxatini pdf faylda yuklash")) {
            SendDocument message = new SendDocument();
            message.setDocument(new InputFile(Objects.requireNonNull(FileController.generateCustomersPDF())));
            message.setChatId(ADMIN);
            MY_TELEGRAM_BOT.sendMsg(message);
        }
        if (message.getText().equals("Barcha productlarni excel fileda yuklash")) {
            SendDocument document = new SendDocument();
            document.setDocument(new InputFile(Objects.requireNonNull(FileControllerExcel.generateCustomersPDF())));
            document.setChatId(ADMIN);
            MY_TELEGRAM_BOT.sendMsg(document);
        }
    }

    public void notificationToAdmin(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ADMIN);
        sendMessage.setText(message);
        MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }
}
