package pandev.bot.exchanger.app.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pandev.bot.exchanger.app.service.impl.ExchangerBotServiceImpl;

import java.util.concurrent.CompletableFuture;
@Component
@RequiredArgsConstructor
@Slf4j
public class BotRunner {
    private final TelegramBot bot;
    private final ExchangerBotServiceImpl exchangerBotServiceImpl;
    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(this::startListeningForUpdates);
    }

    private void startListeningForUpdates() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                exchangerBotServiceImpl.processUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                log.error(e.response().description(), e.response().errorCode());
            } else {
                log.error(e.toString());
            }
        });
    }
}
