package com.company;

import com.company.container.ComponentContainer;
import com.company.load.WorkWithFiles;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        try {

            WorkWithFiles.readCategories();
            WorkWithFiles.readProducts();
            WorkWithFiles.readCustomers();
            WorkWithFiles.readCartProducts();

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            MyTelegramBot myTelegramBot = new MyTelegramBot();
            ComponentContainer.MY_TELEGRAM_BOT = myTelegramBot;

            telegramBotsApi.registerBot(myTelegramBot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}