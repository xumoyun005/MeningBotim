package com.company.util;

import com.company.enums.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class KeyboardButtonUtil {

    public static ReplyKeyboardMarkup getContactMarkup() {

        KeyboardButton contactButton = getKeyboardButton(
                DemoUtil.SHARE_CONTACT_UZ + " / " + DemoUtil.SHARE_CONTACT_RU);
        contactButton.setRequestContact(true);

        KeyboardRow row = getKeyboardRow(contactButton);

        List<KeyboardRow> rowList = getKeyboardRowList(row);

        return getMarkup(rowList);
    }

    public static KeyboardButton getKeyboardButton(String text) {
        return new KeyboardButton(text);
    }

    public static KeyboardRow getKeyboardRow(KeyboardButton... buttons) {
        return new KeyboardRow(new LinkedList<>(Arrays.asList(buttons)));
    }

    public static List<KeyboardRow> getKeyboardRowList(KeyboardRow... rows) {
        return new LinkedList<>(Arrays.asList(rows));
    }

    public static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> rowList) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setSelective(true);

        markup.setKeyboard(rowList);
        return markup;
    }

    public static ReplyKeyboardMarkup getMenuKeyboard(Language language) {

        KeyboardButton menu = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.MENU_UZ : DemoUtil.MENU_RU);
        KeyboardRow row1 = getKeyboardRow(menu);

        KeyboardButton cart = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.CART_UZ : DemoUtil.CART_RU
        );
        KeyboardButton settings = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.SETTINGS_UZ : DemoUtil.SETTINGS_RU
        );
        KeyboardRow row2 = getKeyboardRow(cart, settings);

        List<KeyboardRow> rowList = getKeyboardRowList(row1, row2);

        return getMarkup(rowList);
    }

    public static ReplyKeyboardMarkup getAdminKeyboard(Language language) {

        KeyboardButton crud = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.CATEGORY_CRUD_UZ : DemoUtil.CATEGORY_CRUD_RU);

        KeyboardButton fcrud = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.EXCEL_UZ : DemoUtil.EXCEL_RU
        );

        KeyboardButton k = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.PDF_UZ : DemoUtil.PDF_RU
        );

        KeyboardRow row = getKeyboardRow(k);

        KeyboardButton pcrud = getKeyboardButton(
                language.equals(Language.UZ) ? DemoUtil.PRODUCT_CRUD_UZ : DemoUtil.PRODUCT_CRUD_RU
        );
        KeyboardRow row2 = getKeyboardRow(crud, pcrud);

        KeyboardRow row1 = getKeyboardRow(fcrud);

        List<KeyboardRow> rowList = getKeyboardRowList(row1, row2, row);

        return getMarkup(rowList);
    }
}
