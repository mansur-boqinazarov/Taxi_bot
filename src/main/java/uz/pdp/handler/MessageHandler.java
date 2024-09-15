package uz.pdp.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.SneakyThrows;
import uz.pdp.db.DatabaseHandler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MessageHandler {
    private static final long TARGET_GROUP_CHAT_ID = -4593184390L;
    private final TelegramBot bot;

    public MessageHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void handleForwardMessage(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        String username = message.from().username();
        User user = message.from();

        if (!DatabaseHandler.isDriver(username) && (!Objects.equals(user.username(), "test_uchun_username") || !Objects.equals(user.username(),"Ortiqov89")) ) {
            bot.execute(new ForwardMessage(-4593184390L , chatId, messageId));
            bot.execute(new DeleteMessage(chatId, messageId));



            SendResponse execute = bot.execute(new SendPhoto(chatId, new java.io.File("src/main/resources/img/img.png"))
                    .caption("𝗔𝘀𝘀𝗮𝗹𝗼𝗺𝘂 𝗔𝗹𝗮𝘆𝗸𝘂𝗺  #" + user.firstName() +
                            "\n\nSIZNING ZAKASINGIZ SHAFYORLAR GURUHIGA TUSHDI✅\nSIZGA ISHONCHLI SHAFYORLARIMIZ ALOQAGA CHIQISHADI✅\n\n" +
                            "BOT ORQALI ZAKAS BERISH: [USTIGA BOSING](https://t.me/teststst)")
                    .parseMode(ParseMode.Markdown));
            TimeUnit.SECONDS.sleep(20);
            bot.execute(new DeleteMessage(chatId, execute.message().messageId()));
        }

    }

    public void handleAdminCommand(long chatId, String text) {
        if ("/start".equals(text)) {
            bot.execute(new SendMessage(chatId, "𝗤𝗼'𝘀𝗵𝗺𝗼𝗾𝗵𝗶 𝗯𝗼'𝗹𝗴𝗮𝗻 𝘂𝘀𝗲𝗿𝗻𝗮𝗺𝗲𝗻𝗶 𝗸𝗶𝗿𝗶𝘁𝗶𝗻𝗴: \n\n𝘕𝘢𝘮𝘶𝘯𝘢: @Java_Dev07"));
        } else if (text.startsWith("@")) {
            String username = text.substring(1);
            if (DatabaseHandler.isDriver(username)) {
                bot.execute(new SendMessage(chatId, "𝗕𝘂 𝗵𝗮𝘆𝗱𝗼𝘃𝗰𝗵𝗶 𝗼𝗹𝗱𝗶𝗻𝗱𝗮𝗻 𝗺𝗮𝘃𝗷𝘂𝗱 !!!"));
            } else {
                DatabaseHandler.storeUsername(username);
                bot.execute(new SendMessage(chatId, "𝙐𝙨𝙚𝙧𝙣𝙖𝙢𝙚𝙡𝙖𝙧 𝙧𝙤'𝙮𝙝𝙖𝙩𝙞𝙜𝙖 𝙨𝙝𝙪 @"+username+" 𝙣𝙞 𝙦𝙤'𝙨𝙝𝙙𝙞𝙣𝙜𝙜𝙞𝙯✅\n\nAgar yana qo'shmoqchi bo'lsanggiz /start buyurg'ini bering"));
                bot.execute(new SendMessage(username, "Sizga shafyorlik huquqi berildi✅"));
            }
        }
    }

}
