package ru.tisov.denis;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends DefaultAbsSender {

    protected Bot(DefaultBotOptions options) {
        super(options);
    }


    public void sendMessage(String message) {
        try {
            execute(new SendMessage(-336773455L, message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotToken() {
        return "706077691:AAFrY1-XcUJdZ2DENrWs27D-U0xyXNhtOMs";
    }

}
