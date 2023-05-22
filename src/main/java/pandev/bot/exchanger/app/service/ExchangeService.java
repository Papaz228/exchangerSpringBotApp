package pandev.bot.exchanger.app.service;

import com.pengrad.telegrambot.TelegramException;

public interface ExchangeService {
    String getUSDExchangeRate() throws TelegramException;

    String getKZTExchangeRate() throws TelegramException;

    String getConverted(String currency, Double amount) throws TelegramException;
}
