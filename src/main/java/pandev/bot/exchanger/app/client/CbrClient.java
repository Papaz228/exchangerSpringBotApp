package pandev.bot.exchanger.app.client;

import com.pengrad.telegrambot.TelegramException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CbrClient {

    @Autowired
    private OkHttpClient client;

    @Value("${cbr.currency.rates.xml.url}")
    private String cbrCurrencyRatesXmlUrl;

    public String getCurrencyRatesXML() throws TelegramException {
        var request = new Request.Builder()
                .url(cbrCurrencyRatesXmlUrl)
                .build();

        try (var response = client.newCall(request).execute()) {
            var body = response.body();
            return body == null ? null : body.string();
        } catch (IOException e) {
            throw new TelegramException(e);
        }
    }
}