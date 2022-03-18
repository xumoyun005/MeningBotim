package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.entity.CartProduct;
import com.company.entity.Customer;
import com.company.entity.Product;
import com.company.enums.Language;
import com.company.service.CartProductService;
import com.company.service.CustomerService;
import com.company.service.ProductService;
import com.company.util.DemoUtil;
import com.company.util.InlineKeyboardButtonUtil;
import com.company.util.KeyboardButtonUtil;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainController {


    private final AdminController adminController = new AdminController(new Message());

    public void handleText(User user, String text) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        Customer customer = CustomerService.getCustomerById(String.valueOf(user.getId()));
        Language language = Language.UZ;

        if(customer != null){
            language = customer.getLanguage();
        }

        switch (text) {
            case "/start":

                if (customer == null) {
                    CustomerService.addCustomer(String.valueOf(user.getId()));

                    String messageText = String.format("ID: %s\nFirst name:%s\n" +
                                    "Last name: %s\nUsername: %s\n\nclicked START.",
                            user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
                    adminController.notificationToAdmin(messageText);
                }


                sendMessage.setText("<b>Assalomu alaykum!</b>\n" +
                        "Raqamingizmi jo'nating.");
                sendMessage.setParseMode(ParseMode.HTML);

                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMarkup());

                break;
            case DemoUtil.SETTINGS_UZ:
            case DemoUtil.SETTINGS_RU: {

                assert customer != null;
                sendMessage.setText(
                        (language.equals(Language.UZ) ? "Foydalanuvchi: " : "Пользователь: ") + customer.getFirstName()
                );

                InlineKeyboardButton button = InlineKeyboardButtonUtil.button(
                        language.equals(Language.UZ) ? DemoUtil.CHANGE_LANG_UZ : DemoUtil.CHANGE_LANG_RU,
                        DemoUtil.CHANGE_LANG
                );

                List<InlineKeyboardButton> row = InlineKeyboardButtonUtil.row(button);
                List<List<InlineKeyboardButton>> rowList = InlineKeyboardButtonUtil.rowList(row);

                InlineKeyboardMarkup inlineMarkup = InlineKeyboardButtonUtil.inlineMarkup(rowList);
                sendMessage.setReplyMarkup(inlineMarkup);

                break;
            }
            case DemoUtil.CART_UZ:
            case DemoUtil.CART_RU:
                List<CartProduct> cartProductList = CustomerService.getCartProducts(String.valueOf(user.getId()));
                if (cartProductList == null || cartProductList.isEmpty()) {
                    if (language.equals(Language.UZ)) {
                        sendMessage.setText("Savatcha hozircha bo`sh");
                    } else {
                        sendMessage.setText("Ваша корзина пуста");
                    }
                } else {
                    double total = 0;
                    StringBuilder messageText = new StringBuilder(language.equals(Language.UZ) ? "🛒 Savatchada: \n\n" : "🛒 В корзине:\n\n");

                    for (CartProduct cartProduct : cartProductList) {
                        Product product = cartProduct.getProduct();
                        messageText.append(product.getNameUz()).append("\n");
                        messageText.append(cartProduct.getQuantity()).append(" ✖ ").append(product.getPrice());
                        messageText.append(" = ").append(cartProduct.getQuantity() * product.getPrice()).append("\n\n");

                        total += (cartProduct.getQuantity() * product.getPrice());
                    }

                    messageText.append(language.equals(Language.UZ) ? "\uD83D\uDCB8 Jami mahsulotlar: " : "\uD83D\uDCB8 Общая сумма: ");
                    messageText.append(total);

                    sendMessage.setText(messageText.toString());

                    InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardButtonUtil.cartMenu(language, cartProductList);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                }
                break;
            case DemoUtil.MENU_UZ:
            case DemoUtil.MENU_RU:
                sendMessage.setText(
                        language.equals(Language.UZ) ? DemoUtil.CATEGORY_HEADER_UZ : DemoUtil.CATEGORY_HEADER_RU
                );
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.categoryMenu(language));
                break;
            case "/location": {

                sendMessage.setText("Location jo'nating: ");

                KeyboardButton button = new KeyboardButton();
                button.setText("Send location");
                button.setRequestLocation(true);

                KeyboardRow row = new KeyboardRow();
                row.add(button);

                List<KeyboardRow> keyboardRowList = new LinkedList<>();
                keyboardRowList.add(row);

                ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRowList);
                markup.setResizeKeyboard(true);
                markup.setSelective(true);

                sendMessage.setReplyMarkup(markup);

                break;
            }
            case "/image":

                sendMessage.setText(text);

                Collections.shuffle(ComponentContainer.MY_TELEGRAM_BOT.images);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(user.getId()));
                sendPhoto.setPhoto(new InputFile(ComponentContainer.MY_TELEGRAM_BOT.images.get(0)));
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
                break;
            case "/remove_keyboard":

                sendMessage.setText(text);

                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                break;
            case "/document":

                sendMessage.setText(text);
                if (String.valueOf(user.getId()).equals(ComponentContainer.ADMIN)){

                SendDocument sendDocument = new SendDocument();
                sendDocument.setCaption("Foydalanuvchilar:");

                sendMessage.setText(String.valueOf(FileController.generateCustomersPDF()));
             sendDocument.setChatId(ComponentContainer.ADMIN);

                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument);
                break;
                }
            default:
                sendMessage.setText(text);
                break;
        }

        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public void handleCallback(User user, Message message, String data) {
        Customer customer = CustomerService.getCustomerById(String.valueOf(user.getId()));
        Language language = Language.UZ;

        if(customer != null){
            language = customer.getLanguage();
        }

        if(data.equals(DemoUtil.CHANGE_LANG)){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            assert customer != null;
            if(customer.getLanguage().equals(Language.UZ)){
                customer.setLanguage(Language.RU);
            }else{
                customer.setLanguage(Language.UZ);
            }

            sendMainMenu(String.valueOf(user.getId()));

        }else if(data.equals(DemoUtil.BACK_FROM_PRODUCT_LIST)){

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(
                    language.equals(Language.UZ) ? DemoUtil.CATEGORY_HEADER_UZ : DemoUtil.CATEGORY_HEADER_RU
            );
            editMessageText.setChatId(String.valueOf(user.getId()));
            editMessageText.setMessageId(message.getMessageId());

            editMessageText.setReplyMarkup(InlineKeyboardButtonUtil.categoryMenu(language));
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(editMessageText);

        }else if(data.startsWith(DemoUtil.BACK_FROM_COUNT_PRODUCT+"/")){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            Integer categoryId = Integer.parseInt(data.split("/")[1]);
            List<Product> productList = ProductService.getProductsByCategoryId(categoryId);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(
                    language.equals(Language.UZ) ? DemoUtil.CATEGORY_HEADER_UZ : DemoUtil.CATEGORY_HEADER_RU
            );
            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardButtonUtil.productMenu(language, productList);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);

        }else if(data.startsWith("category/")){
            Integer categoryId = Integer.parseInt(data.split("/")[1]);

            List<Product> productList = ProductService.getProductsByCategoryId(categoryId);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(
                    language.equals(Language.UZ) ? DemoUtil.CATEGORY_HEADER_UZ : DemoUtil.CATEGORY_HEADER_RU
            );
            editMessageText.setChatId(String.valueOf(user.getId()));
            editMessageText.setMessageId(message.getMessageId());

            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardButtonUtil.productMenu(language, productList);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(editMessageText);
        }else if(data.startsWith("product/")){
            Integer productId = Integer.parseInt(data.split("/")[1]);
            Product product = ProductService.getProductById(productId);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            String caption = language.equals(Language.UZ) ? product.getNameUz() : product.getNameRu();
            caption += " : "+product.getPrice()+"\n\n";
            caption += language.equals(Language.UZ) ? "Mahsulot sonini tanlang:" : "Выберите количество продукта:";

            SendPhoto sendPhoto = new SendPhoto();

            sendPhoto.setChatId(String.valueOf(user.getId()));
            sendPhoto.setCaption(caption);
            sendPhoto.setPhoto(new InputFile(
                    new File(ComponentContainer.PATH+product.getImageUrl())
            ));

            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardButtonUtil.selectCountMenu(language, product);
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);

            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
        }else if(data.startsWith("count/")){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            String[] split = data.split("/");
            Integer productId = Integer.parseInt(split[1]);
            Integer quantity = Integer.parseInt(split[2]);

            CartProductService.add(String.valueOf(user.getId()), productId, quantity);

            handleText(user, DemoUtil.CART_UZ);
        }
        else if(data.startsWith(DemoUtil.REMOVE_CART_PRODUCT)){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            String[] split = data.split("/");
            Integer cartProductId = Integer.parseInt(split[1]);

            CartProductService.delete(cartProductId);

            handleText(user, DemoUtil.CART_UZ);
        } else if(data.startsWith(DemoUtil.CONTINUE)){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            handleText(user, DemoUtil.MENU_UZ);

        }else if(data.startsWith(DemoUtil.COMMIT)){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            CartProductService.delete(String.valueOf(user.getId()));

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(
                    language.equals(Language.UZ) ? "Buyurtma qabul qilindi." : "Заказ был принят."
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        } else if(data.startsWith(DemoUtil.CANCEL)){

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(user.getId()));
            deleteMessage.setMessageId(message.getMessageId());
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(deleteMessage);

            CartProductService.delete(String.valueOf(user.getId()));

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getId()));
            sendMessage.setText(
                    language.equals(Language.UZ) ? "Buyurtma bekor qilindi." : "Заказ отменен."
            );
            ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
        }

    }

    public void sendMainMenu(String userId){
        Language language = CustomerService.getLanguageByUserId(userId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userId);
        sendMessage.setText(
                language.equals(Language.UZ) ? DemoUtil.MENU_HEADER_UZ : DemoUtil.MENU_HEADER_RU
        );

        ReplyKeyboardMarkup markup = KeyboardButtonUtil.getMenuKeyboard(language);
        sendMessage.setReplyMarkup(markup);

        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }
}
