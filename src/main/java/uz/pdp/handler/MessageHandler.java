package uz.pdp.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.db.DatabaseHandler;

public class MessageHandler {
    private static final long TARGET_GROUP_CHAT_ID = -1002197254829L;
    private TelegramBot bot;

    public MessageHandler(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleForwardMessage(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        String username = message.from().username();
        String userFirstName = message.from().firstName();
        User user = message.from();

        // Forward the message to the target group
        bot.execute(new ForwardMessage(TARGET_GROUP_CHAT_ID, chatId, messageId));


        // Delete the original message
        bot.execute(new DeleteMessage(chatId, messageId));

        String userProfileLink = "tg://user?id=" + user.id();


        bot.execute(new SendMessage(chatId, "Assalomu Alaykum  ["+user.firstName()+"]("+userProfileLink+")"+"\nBuyurtmangiz Haydovchilar guruhiga yuborildi\nIshonchli haydovchilar tez orada siz bilan bog‘lanadi.").parseMode(ParseMode.Markdown));
    }

    public void handleAdminCommand(long chatId, String text) {
        if ("/start".equals(text)) {
            bot.execute(new SendMessage(chatId, "Chat id yuboring:"));
        } else if (text.matches("-?\\d+")) {
            long newChatId = Long.parseLong(text);


            if (DatabaseHandler.isDriver(newChatId)) {
                bot.execute(new SendMessage(chatId, "Bu haydovchi oldindan mavjud!!!"));
            } else {
                DatabaseHandler.storeChatId(newChatId);
                bot.execute(new SendMessage(chatId, "Chat ID saqlandi: " + newChatId));
                bot.execute(new SendMessage(newChatId,"Siz haydovchilik huquqi berildi✅"));
            }
        }
    }
}
