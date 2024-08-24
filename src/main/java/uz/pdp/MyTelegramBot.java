package uz.pdp;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.db.DatabaseHandler;
import uz.pdp.handler.DriverHandler;
import uz.pdp.handler.MessageHandler;
import uz.pdp.handler.UserHandler;

public class MyTelegramBot {
    private static final String BOT_TOKEN = "7539023418:AAE-X6wbMzb9iBSudlDKrdydJgAub8nVnzQ";
    private static final long SOURCE_GROUP_CHAT_ID = -1002226394628L;
    private static final long SUPER_ADMIN_CHAT_ID = 5127045086L;
    public static final TelegramBot bot = new TelegramBot(BOT_TOKEN);
    public static void main(String[] args) {

        MessageHandler messageHandler = new MessageHandler(bot);
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                Message message = update.message();
                if (message != null) {
                    long chatId = message.chat().id();
                    String text = message.text();

                    if (chatId == SUPER_ADMIN_CHAT_ID) {
                        messageHandler.handleAdminCommand(chatId, text);
                    } else if (chatId == SOURCE_GROUP_CHAT_ID) {
                        messageHandler.handleForwardMessage(message);
                    }else if (DatabaseHandler.isDriver(chatId)){
                        DriverHandler.handle(update);
                    }else if (!DatabaseHandler.isDriver(chatId)){
                        UserHandler.handle(update);
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
