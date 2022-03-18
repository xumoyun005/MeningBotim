package com.company.util;

import com.company.db.Database;
import com.company.entity.CartProduct;
import com.company.entity.Category;
import com.company.entity.Product;
import com.company.enums.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineKeyboardButtonUtil {


    public static InlineKeyboardMarkup categoryMenu(Language language){

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (Category category : Database.CATEGORIES) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            if(language.equals(Language.UZ)){
                button.setText(category.getNameUz());
            }else{
                button.setText(category.getNameRu());
            }

            button.setCallbackData("category/"+category.getId());
            List<InlineKeyboardButton> row = row(button);

            rowList.add(row);
        }

        return inlineMarkup(rowList);
    }

    public static InlineKeyboardMarkup productMenu(Language language, List<Product> productList) {

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (Product product : productList) {
            InlineKeyboardButton button = new InlineKeyboardButton(
                    language.equals(Language.UZ) ? product.getNameUz() : product.getNameRu()
            );
            button.setCallbackData("product/"+product.getId());
            List<InlineKeyboardButton> row = row(button);

            rowList.add(row);
        }

        InlineKeyboardButton back = button(
                language.equals(Language.UZ) ? DemoUtil.BACK_UZ : DemoUtil.BACK_RU,
                DemoUtil.BACK_FROM_PRODUCT_LIST
        );
        List<InlineKeyboardButton> row = row(back);
        rowList.add(row);

        return inlineMarkup(rowList);
    }


    public static InlineKeyboardButton button(String demo, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton(demo);
        button.setCallbackData(callback);
        return button;
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton... buttons) {
        return new LinkedList<>(Arrays.asList(buttons));
    }

    @SafeVarargs
    public static List<List<InlineKeyboardButton>> rowList(List<InlineKeyboardButton>... rows) {
        return new LinkedList<>(Arrays.asList(rows));
    }

    public static InlineKeyboardMarkup inlineMarkup(List<List<InlineKeyboardButton>> keyboard) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup selectCountMenu(Language language, Product product) {
        String suffix = language.equals(Language.UZ) ? " ta" : " шт.";

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (int i = 1; i <= 7 ; i+=3) {
            InlineKeyboardButton button1 = button(i + suffix, "count/" + product.getId() + "/" + i);
            InlineKeyboardButton button2 = button((i+1) + suffix, "count/" + product.getId() + "/" + (i+1));
            InlineKeyboardButton button3 = button((i+2) + suffix, "count/" + product.getId() + "/" + (i+2));

            List<InlineKeyboardButton> row = row(button1, button2, button3);

            rowList.add(row);
        }

        InlineKeyboardButton back = button(
                language.equals(Language.UZ) ? DemoUtil.BACK_UZ : DemoUtil.BACK_RU,
                DemoUtil.BACK_FROM_COUNT_PRODUCT+"/"+product.getCategory().getId()
        );
        List<InlineKeyboardButton> row = row(back);
        rowList.add(row);

        return inlineMarkup(rowList);
    }

    public static InlineKeyboardMarkup cartMenu(Language language,  List<CartProduct> cartProductList) {
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (CartProduct cartProduct : cartProductList) {
            InlineKeyboardButton button = new InlineKeyboardButton(
                    ("❌  ")+(language.equals(Language.UZ) ? cartProduct.getProduct().getNameUz() :
                            cartProduct.getProduct().getNameRu())
            );
            button.setCallbackData(DemoUtil.REMOVE_CART_PRODUCT+"/"+cartProduct.getId());

            List<InlineKeyboardButton> row = row(button);

            rowList.add(row);
        }

        InlineKeyboardButton continueButton = button(
                ("🔁  ")+(language.equals(Language.UZ) ? DemoUtil.CONTINUE_UZ : DemoUtil.CONTINUE_RU),
                DemoUtil.CONTINUE
        );
        List<InlineKeyboardButton> row1 = row(continueButton);

        InlineKeyboardButton commitButton = button(
                ("✔  ")+(language.equals(Language.UZ) ? DemoUtil.COMMIT_UZ : DemoUtil.COMMIT_RU),
                DemoUtil.COMMIT
        );
        InlineKeyboardButton cancelButton = button(
                ("🚫  ")+(language.equals(Language.UZ) ? DemoUtil.CANCEL_UZ : DemoUtil.CANCEL_RU),
                DemoUtil.CANCEL
        );
        List<InlineKeyboardButton> row2 = row(commitButton, cancelButton);

        rowList.add(row1);
        rowList.add(row2);

        return inlineMarkup(rowList);
    }
}
