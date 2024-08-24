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
    private static final long SUPER_ADMIN_CHAT_ID = 5127045086L;
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


        bot.execute(new SendMessage(chatId, "𝗔𝘀𝘀𝗮𝗹𝗼𝗺𝘂 𝗔𝗹𝗮𝘆𝗸𝘂𝗺  [" + user.firstName() + "](" + userProfileLink + ")" + "\n𝗕𝘂𝘆𝘂𝗿𝘁𝗺𝗮𝗻𝗴𝗴𝗶𝘇 𝗛𝗮𝘆𝗱𝗼𝘃𝗰𝗵𝗶𝗹𝗮𝗿 𝗴𝘂𝗿𝘂𝗵𝗶𝗴𝗮 𝘆𝘂𝗯𝗼𝗿𝗶𝗹𝗱𝗶\n𝗜𝘀𝗵𝗼𝗻𝗰𝗵𝗹𝗶 𝗛𝗮𝘆𝗱𝗼𝘃𝗰𝗵𝗶𝗹𝗮𝗿 𝘁𝗲𝘇 𝗼𝗿𝗮𝗱𝗮 𝘀𝗶𝘇 𝗯𝗶𝗹𝗮𝗻 𝗯𝗼𝗴'𝗹𝗮𝗻𝗮𝗱𝗶").parseMode(ParseMode.Markdown));

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
