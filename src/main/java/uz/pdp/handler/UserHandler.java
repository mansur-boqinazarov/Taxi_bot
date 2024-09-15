    package uz.pdp.handler;

    import com.pengrad.telegrambot.model.Message;
    import com.pengrad.telegrambot.model.Update;
    import com.pengrad.telegrambot.model.User;
    import com.pengrad.telegrambot.model.request.KeyboardButton;
    import com.pengrad.telegrambot.model.request.ParseMode;
    import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
    import com.pengrad.telegrambot.request.SendMessage;
    import uz.pdp.model.Order;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;

    import static uz.pdp.MyTelegramBot.bot;

    public class UserHandler {
        public static final Update update = new Update();;
        private static final Map<Long, Order> userOrders = new HashMap<>();
        private static final Map<Long, String> userSteps = new HashMap<>();

        public static void handle(Update update) {
            if (update.message() != null) {
                handleTextMessage(update);
            }
        }

        private static void handleTextMessage(Update update) {
            Message message = update.message();
            User user = message.from();
            String text = message.text();
            Long chatId = message.chat().id();

            if (Objects.equals(text, "/start")) {
                startOrderProcess(chatId, user);
            } else if (userSteps.containsKey(chatId)) {
                String step = userSteps.get(chatId);
                switch (step) {
                    case "waiting_for_direction":
                        handleDirectionInput(chatId, text);
                        break;
                    case "waiting_for_number_of_people":
                        handleNumberOfPeopleInput(chatId, text);
                        break;
                    case "waiting_for_phone_number":
                        handlePhoneNumberInput(chatId, text,update);
                        break;
                    default:

                        break;
                }
            } else {
            }
        }

        private static void startOrderProcess(Long chatId, User user) {
            SendMessage sendMessage = new SendMessage(chatId, "Salom, " + user.firstName() + "!\n" +
                    "Yonalish manzilni kiritng:\n\nNamuna:  𝘙𝘢𝘱𝘲𝘰𝘯𝘥𝘢𝘯 𝘛𝘰𝘴𝘩𝘬𝘦𝘯𝘵𝘨𝘢");
            bot.execute(sendMessage);
            userSteps.put(chatId, "waiting_for_direction");
            userOrders.put(chatId, new Order());
        }

        private static void handleDirectionInput(Long chatId, String text) {
            Order order = userOrders.get(chatId);
            order.setDirection(text);

            // Create keyboard buttons for number of people
            KeyboardButton b1 = new KeyboardButton("1️⃣");
            KeyboardButton b2 = new KeyboardButton("2️⃣");
            KeyboardButton b3 = new KeyboardButton("3️⃣");
            KeyboardButton b4 = new KeyboardButton("4️⃣");
            KeyboardButton b5 = new KeyboardButton("📦Pochta");
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(b1, b2, b3, b4, b5)
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(true);

            SendMessage sendMessage = new SendMessage(chatId, "👥Nechta kishi sizlar?")
                    .replyMarkup(keyboardMarkup);
            bot.execute(sendMessage);
            userSteps.put(chatId, "waiting_for_number_of_people");
        }

        private static void handleNumberOfPeopleInput(Long chatId, String text) {
            Order order = userOrders.get(chatId);
            order.setNumberOfPeople(text);

            SendMessage sendMessage = new SendMessage(chatId, "📞Telefon nomeringizni yuboring:\n\nNamuna: +998912345678");
            bot.execute(sendMessage);
            userSteps.put(chatId, "waiting_for_phone_number");
        }

        private static void handlePhoneNumberInput(Long chatId, String text,Update update) {
            Order order = userOrders.get(chatId);
            order.setPhoneNumber(text);
            User user = update.message().from();
            String userProfileLink = "tg://user?id=" + user.id();
            // Send the collected data to the group
            Long groupId = -4593184390L;
            String orderDetails = "🚗YANGI BUYURTMA KELDI\n\n" +
                    "🛣Yo'nalish: " + order.getDirection() + "\n" +
                    "👥Odamlar soni: " + order.getNumberOfPeople() + "\n" +
                    "📞Telefon raqami: " + order.getPhoneNumber()+"\n"+
                    "📱Telegram: ["+user.firstName()+"]("+userProfileLink+")";
            SendMessage sendMessageToGroup = new SendMessage(groupId, orderDetails).parseMode(ParseMode.Markdown);
            bot.execute(sendMessageToGroup);

            // Confirm to the user
            SendMessage confirmationMessage = new SendMessage(chatId, "𝗕𝘂𝘆𝘂𝗿𝘁𝗺𝗮 𝗾𝗮𝗯𝘂𝗹 𝗾𝗶𝗹𝗶𝗻𝗱𝗶 ✅\n𝗧𝗲𝘇 𝗼𝗿𝗮𝗱𝗮 𝗵𝗮𝘆𝗱𝗼𝘃𝗰𝗵𝗶𝗹𝗮𝗿 𝘀𝗶𝘇 𝗯𝗶𝗹𝗮𝗻 𝗯𝗼𝗴'𝗹𝗮𝗻𝗮𝗱𝗶.\n\nAgar yana zakas bermoqchi bo'lsanggiz /start buyug'ini bering");
            bot.execute(confirmationMessage);

            // Clean up
            userOrders.remove(chatId);
            userSteps.remove(chatId);
        }

    }
