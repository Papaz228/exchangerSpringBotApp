package pandev.bot.exchanger.app.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface ExchangerBotService {
    void processUpdate(Update update);
    void convertCommand(Long chatId);
    void convertUsdCommand(Long chatId);
    void convertKztCommand(Long chatId);
    void convertAmount(Long chatId, String amount);
    void startCommand(Long chatId, String userName);
    void usdCommand(Long chatId);
    void kztCommand(Long chatId);
    void helpCommand(Long chatId);
    void unknownCommand(Long chatId);
    void sendMessage(SendMessage sendMessage);
}
