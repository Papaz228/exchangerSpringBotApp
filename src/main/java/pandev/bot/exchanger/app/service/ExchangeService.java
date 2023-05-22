package pandev.bot.exchanger.app.service;

public interface ExchangeService {
    String getUSDExchangeRate();
    String getKZTExchangeRate();
    String getConverted(String currency, Double amount);
}
