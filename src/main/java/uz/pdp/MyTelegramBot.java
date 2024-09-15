package uz.pdp;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.db.DatabaseHandler;
import uz.pdp.handler.MessageHandler;
import uz.pdp.handler.UserHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTelegramBot {
    private static final String BOT_TOKEN = "7273639407:AAEAgK-VmFNCNbiEXKP_scp3SPe-UuUvFQ8";
    private static final long SOURCE_GROUP_CHAT_ID = -1001968655481L;
    private static final long SUPER_ADMIN_CHAT_ID = 5127045086L;
    public static final TelegramBot bot = new TelegramBot(BOT_TOKEN);
    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {

        MessageHandler messageHandler = new MessageHandler(bot);
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                CompletableFuture.runAsync(() -> {
                    Message message = update.message();
                    if (message != null) {
                        long chatId = message.chat().id();
                        String text = message.text();

                        if (chatId == SUPER_ADMIN_CHAT_ID || chatId == 5126567505L) {
                            messageHandler.handleAdminCommand(chatId, text);
                        } else if (chatId == SOURCE_GROUP_CHAT_ID) {
                            messageHandler.handleForwardMessage(message);
                        } else {
                            UserHandler.handle(update);
                        }
                    }
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}