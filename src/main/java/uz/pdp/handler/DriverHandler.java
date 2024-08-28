package uz.pdp.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.Objects;

import static uz.pdp.MyTelegramBot.bot;

public class DriverHandler {
    private static String manzillar = "";
    private static String ketishVaqti = "";
    private static String yolovchilar = "";
    private static String pochtaQabul = "";
    private static String mashinaTuri = "";
    private static String telefonRaqam = "";
    private static String qoshimchaMalumot = "";

    public static void handle(Update update) {
        User user = update.message().from();
        Message message = update.message();
        String text = message.text();
        Long chatId = message.chat().id();

        if (Objects.equals(text, "/start")) {
            bot.execute(new SendMessage(chatId, "Xush kelibsiz!\n📍Yo'nalish manzilni kiritng?"));
        } else if (manzillar.isEmpty()) {
            manzillar = text;
            bot.execute(new SendMessage(chatId, "🕒 Qachon yo'lga chiqasiz?"));
        } else if (ketishVaqti.isEmpty()) {
            ketishVaqti = text;

            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                    new String[]{"1️⃣", "2️⃣", "3️⃣", "4️⃣"})
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true);
            bot.execute(new SendMessage(chatId, "👥 Nechta yo'lovchi kerak?").replyMarkup(keyboard));
        } else if (yolovchilar.isEmpty()) {

            yolovchilar = text;

            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                    "Po'chta olinadi", "Po'chta olinmaydi")
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true);
            bot.execute(new SendMessage(chatId, "✉️ Pochtani qabul qilasizmi?").replyMarkup(keyboard));
        } else if (pochtaQabul.isEmpty()) {

            pochtaQabul = text;
            bot.execute(new SendMessage(chatId, "🚗 Qanday mashina?"));
        } else if (mashinaTuri.isEmpty()) {

            mashinaTuri = text;
            bot.execute(new SendMessage(chatId, "📞 Telefon raqamingizni kiriting:"));
        } else if (telefonRaqam.isEmpty()) {

            telefonRaqam = text;

            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                    "Ha", "Yo'q")
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true);
            bot.execute(new SendMessage(chatId, "ℹ️ Qo'shimcha ma'lumotlar bormi?").replyMarkup(keyboard));
        } else if (qoshimchaMalumot.isEmpty() && text.equals("Ha")) {

            bot.execute(new SendMessage(chatId, "ℹ️ Qo'shimcha ma'lumotni kiriting:"));
            qoshimchaMalumot = text;
        } else if (qoshimchaMalumot.isEmpty() && text.equals("Yo'q")) {

            sendCollectedData(chatId, false,update);
        } else if (!qoshimchaMalumot.isEmpty()) {

            qoshimchaMalumot = text;
            sendCollectedData(chatId, true,update);
        }
    }

    private static void sendCollectedData(Long chatId, boolean includeExtraInfo,Update update) {
        String userProfileLink = "tg://user?id=" + update.message().from().id();
        String userFirstName = update.message().from().firstName();
        String telegram = "["+userFirstName+"]("+userProfileLink+")";
        String messageToGroup = String.format(
                """
                        📍 Yo'nalish:  %s
                        🕒 Ketish vaqti:  %s
                        👥 Yo'lovchilar: %s kishi olinadi
                        📦 %s
                        🚗 Mashina:  %s
                        📞 Telefon:  %s
                        📱 Telegram: %s
                        """,
                manzillar, ketishVaqti, yolovchilar, pochtaQabul, mashinaTuri, telefonRaqam,telegram
        );

        if (includeExtraInfo) {
            messageToGroup += String.format("\nℹ️ Qo'shimcha ma'lumot: %s", qoshimchaMalumot);
        }


        bot.execute(new SendMessage(-1002226394628L, messageToGroup).parseMode(ParseMode.Markdown));

        manzillar = "";
        ketishVaqti = "";
        yolovchilar = "";
        pochtaQabul = "";
        mashinaTuri = "";
        telefonRaqam = "";
        qoshimchaMalumot = "";


        bot.execute(new SendMessage(chatId, "Guruhga yuborildi ✅"));
        bot.execute(new SendMessage(chatId, "Qayta zakaz berish uchun /start ni bosing!!!"));
    }
}
