package com.company;

import com.company.container.ComponentContainer;
import com.company.controller.AdminController;
import com.company.controller.MainController;
import com.company.service.CustomerService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot {

    public final List<String> images = new LinkedList<>();


    public MyTelegramBot() {
        images.add("AgACAgIAAxkBAAIEVmIg0yXhhU40oDnMO95vYfHCerkUAAKAuDEb7y0JSUkFWBuWJT6BAQADAgADeQADIwQ");
        images.add("AgACAgIAAxkBAAIEWGIg0yjpniOwR5OLMo0SNnRqN4UeAAJduDEbBnXxSK8Ve42W5p0GAQADAgADeQADIwQ");
        images.add("AgACAgIAAxkBAAIEWmIg0ygRwKezlUgVpyX0iOOTB8agAAJ7uTEbgl8JSdxM1AVPftvtAQADAgADeAADIwQ");
    }

    private final MainController mainController = new MainController();

    @Override
    public String getBotToken() {
        return "5205197103:AAHEsWgecCJa6gVKBD8c5xfekIhEAYDKQmg";
    }

    @Override
    public String getBotUsername() {
        return "fAs_Fod_BoT";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();

            if (String.valueOf(message.getChatId()).equals(ComponentContainer.ADMIN)) {

                AdminController adminController1 = new AdminController(message);
                adminController1.start();

            } else {

                if (message.hasText()) {
                    String text = message.getText();

                    log(user, text);

                    mainController.handleText(user, text);


                } else if (message.hasPhoto()) {

                    List<PhotoSize> photoSizeList = message.getPhoto();

                    String fileId = photoSizeList.get(photoSizeList.size() - 1).getFileId();

                    images.add(fileId);
                    Collections.shuffle(images);

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(String.valueOf(user.getId()));
                    sendPhoto.setPhoto(new InputFile(images.get(0)));
                    ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);

                } else if (message.hasContact()) {
                    Contact contact = message.getContact();

                    CustomerService.setContactToCustomer(contact);

                    mainController.sendMainMenu(String.valueOf(user.getId()));

                } else if (message.hasLocation()) {

                    Location location = message.getLocation();

                    String messageStr = "Location:\n";
                    messageStr += "\nLat: " + location.getLatitude();
                    messageStr += "\nLng: " + location.getLongitude();
                    messageStr += "\nUser: " + message.getFrom().getId() + " " + message.getFrom().getFirstName();

                }
            }
        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (String.valueOf(callbackQuery.getFrom().getId()).equals(ComponentContainer.ADMIN)) {
                AdminController.workWithCallbackQuery();
            }
            else {
                Message message1 = callbackQuery.getMessage();
                User user1 = callbackQuery.getFrom();
                String data = callbackQuery.getData();

                mainController.handleCallback(user1, message1, data);
            }
        }
    }

    public void log(User user, String message) {
        String format = String.format("ID: %s, from: %s %s  text: \"%s\"",
                user.getId(), user.getFirstName(), user.getLastName(), message);
        System.out.println(format);
    }

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(DeleteMessage deleteMessage) {
        try {
             execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
