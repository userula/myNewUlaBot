import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }
    }

    private static boolean weather = false;

    public void sendMsg(Message msg, String t)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId().toString());
        sendMessage.setReplyToMessageId(msg.getMessageId());
        sendMessage.setText(t);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(Message msg, String t, String url)
    {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.enableNotification();
        sendPhoto.setChatId(msg.getChatId().toString());
        sendPhoto.setReplyToMessageId(msg.getMessageId());
        sendPhoto.setPhoto(url);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sm)
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sm.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboard1Row = new KeyboardRow();
        KeyboardRow keyboard2Row = new KeyboardRow();

        keyboard2Row.add(new KeyboardButton("/weather"));

        keyboard1Row.add(new KeyboardButton("/help"));
        keyboard1Row.add(new KeyboardButton("/settings"));

        keyboardRowList.add(keyboard1Row);
        keyboardRowList.add(keyboard2Row);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();

        Message msg = update.getMessage();

        String user = msg.getChat().getFirstName();

        if(msg != null && msg.hasText())
        {
            if(weather == false)
            {
                switch (msg.getText())
                {
                    case "/start": sendMsg(msg, "Hi " + user);
                        break;
                    case "Hi": sendMsg(msg, "How are you?");
                        break;
                    case "hi": sendMsg(msg, "How are you?");
                        break;
                    case "hello": sendMsg(msg, "How are you?");
                        break;
                    case "Hello": sendMsg(msg, "How are you?");
                        break;
                    case "/help": sendMsg(msg, "Sorry, I am lazyy)");
                        break;
                    case "/settings": sendMsg(msg, "Ниче не надо менять, я идеальный!!!");
                        break;
                    case "/weather": weather = true;
                        sendMsg(msg, "Write city name:");
                        break;
                    default: sendMsg(msg, "Больше ниче не знаю");
                }
            }
            else if(msg.getText().equals("/stop"))
            {
                weather = false;
                sendMsg(msg, "Weather stopped!");
            }
            else if(weather == true)
            {
                Weather w = new Weather();
                try {
                    sendMsg(msg, w.getWeather(msg.getText(), model));
                    sendMsg(msg, "", "https://openweathermap.org/img/w/"+ w.getM().getIcon() + ".png");
                    sendMsg(msg, "Click '/stop' if you want to stop weather function.");
                } catch (IOException e) {
                    sendMsg(msg, "City is not found!");
                }
            }
        }
    }

    public String getBotUsername() {
        return "myNewUlaBo";
    }

    public String getBotToken() {
        return "1239266158:AAF9ES_M4vvKF2zv7pIZCZAtruMfjAPHHj4";
    }
}
