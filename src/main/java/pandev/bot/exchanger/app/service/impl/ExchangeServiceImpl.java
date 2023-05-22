package pandev.bot.exchanger.app.service.impl;

import com.pengrad.telegrambot.TelegramException;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import pandev.bot.exchanger.app.client.CbrClient;
import pandev.bot.exchanger.app.constant.Command;
import pandev.bot.exchanger.app.service.ExchangeService;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private CbrClient client;

    @Nullable
    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws TelegramException {
        var source = new InputSource(new StringReader(xml));
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new TelegramException(e);
        }
    }

    @Override
    public String getUSDExchangeRate() throws TelegramException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, Command.USD_XPATH);
    }

    @Override
    public String getKZTExchangeRate() throws TelegramException {
        var xml = client.getCurrencyRatesXML();
        String value = extractCurrencyValueFromXML(xml, Command.KZT_XPATH);
        value = value.replace(",", ".");
        return String.valueOf(BigDecimal.valueOf(Double.parseDouble(value)).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
    }

    @Override
    public String getConverted(String currency, Double amount) throws TelegramException {
        var xml = client.getCurrencyRatesXML();
        String kzt = extractCurrencyValueFromXML(xml, Command.KZT_XPATH);
        kzt = kzt.replace(",", ".");
        String usd = extractCurrencyValueFromXML(xml, Command.USD_XPATH);
        usd = usd.replace(",", ".");
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(usd)).divide(BigDecimal.valueOf(Double.parseDouble(kzt)), 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100));
        BigDecimal amountDecimal = BigDecimal.valueOf(amount);
        if (currency.equals(Command.CONVERT_USD)) {
            return rate.multiply(amountDecimal).toString();
        }
        return amountDecimal.divide(rate, 4, RoundingMode.DOWN).toString();
    }
}