package pandev.bot.exchanger.app.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pandev.bot.exchanger.app.constant.Command;
import pandev.bot.exchanger.app.constant.MessageView;
import pandev.bot.exchanger.app.service.ExchangeService;
import pandev.bot.exchanger.app.service.ExchangerBotService;
import pandev.bot.exchanger.app.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pandev.bot.exchanger.app.constant.MessageView.INFO_KZT_RATE;
import static pandev.bot.exchanger.app.constant.MessageView.INFO_USD_RATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangerBotServiceImpl implements ExchangerBotService {
    private final UserService userService;
    private final ExchangeService exchangeService;
    private final TelegramBot bot;

    private final Map<Long, String> userStates = new HashMap<>();

    @Override
    public void processUpdate(Update update) {
        if (update == null || update.message() == null) {
            return;
        }
        Long chatId = update.message().chat().id();
        if (Boolean.FALSE.equals(userService.checkUser(chatId))) {
            userService.registerUser(update.message());
        } else {
            userService.updateUser(update.message());
        }
        if (update.message().text() == null) {
            return;
        }
        String message = update.message().text();
        switch (message) {
            case Command.BUTTON_START -> {
                String userName = update.message().chat().username();
                startCommand(chatId, userName);
            }
            case Command.USD -> usdCommand(chatId);
            case Command.BUTTON_HELP -> helpCommand(chatId);
            case Command.KZT -> kztCommand(chatId);
            case Command.CONVERT -> convertCommand(chatId);
            case Command.CONVERT_KZT -> convertKztCommand(chatId);
            case Command.CONVERT_USD -> convertUsdCommand(chatId);
            default -> {
                if (userStates.containsKey(chatId)) {
                    String state = userStates.get(chatId);
                    if (state.equals(Command.CONVERT_KZT) || state.equals(Command.CONVERT_USD)) {
                        convertAmount(chatId, message);
                    }
                } else {
                    unknownCommand(chatId);
                }
            }
        }
    }

    public void convertCommand(Long chatId) {
        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(new KeyboardButton(Command.CONVERT_KZT));
        keyboardButtons.add(new KeyboardButton(Command.CONVERT_USD));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtons.toArray(new KeyboardButton[0]));
        sendMessage(new SendMessage(chatId, MessageView.CHOOSE_CURRENT).replyMarkup(replyKeyboardMarkup));
    }

    public void convertUsdCommand(Long chatId) {
        userStates.put(chatId, Command.CONVERT_USD);
        sendMessage(new SendMessage(chatId, MessageView.WAIT_FOR_INPUT));
    }

    public void convertKztCommand(Long chatId) {
        userStates.put(chatId, Command.CONVERT_KZT);
        sendMessage(new SendMessage(chatId, MessageView.WAIT_FOR_INPUT));
    }

    public void convertAmount(Long chatId, String amount) {
        String selectedCurrency = userStates.get(chatId);

        userStates.remove(chatId);

        String result;
        try {
            result = exchangeService.getConverted(selectedCurrency, Double.valueOf(amount));
        } catch (Exception e) {
            log.error(MessageView.ERROR_COMMAND, e);
            result = "";
        }

        sendMessage(new SendMessage(chatId, result));
    }

    public void startCommand(Long chatId, String userName) {
        sendMessage(new SendMessage(chatId, MessageView.SHOW_START + userName));
    }

    public void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = exchangeService.getUSDExchangeRate();
            formattedText = String.format(INFO_USD_RATE, LocalDate.now(), usd);
        } catch (Exception e) {
            log.error(MessageView.ERROR_COMMAND, e);
            formattedText = MessageView.ERROR_COMMAND;
        }
        sendMessage(new SendMessage(chatId, formattedText));
    }

    public void kztCommand(Long chatId) {
        String formattedText;
        try {
            var kzt = exchangeService.getKZTExchangeRate();
            formattedText = String.format(INFO_KZT_RATE, LocalDate.now(), kzt);
        } catch (Exception e) {
            log.error(MessageView.ERROR_COMMAND, e);
            formattedText = MessageView.ERROR_COMMAND;
        }
        sendMessage(new SendMessage(chatId, formattedText));
    }

    public void helpCommand(Long chatId) {
        sendMessage(new SendMessage(chatId, MessageView.SHOW_HELP));
    }

    public void unknownCommand(Long chatId) {
        sendMessage(new SendMessage(chatId, MessageView.SHOW_UNDEFINED));
    }

    public void sendMessage(SendMessage sendMessage) {
        bot.execute(sendMessage);
    }
}
