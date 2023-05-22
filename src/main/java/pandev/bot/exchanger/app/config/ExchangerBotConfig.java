package pandev.bot.exchanger.app.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pandev.bot.exchanger.app.constant.Command;

import java.util.ArrayList;

import static pandev.bot.exchanger.app.constant.MessageView.*;

@Configuration
public class ExchangerBotConfig {
    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String botToken){
        TelegramBot telegramBot = new TelegramBot(botToken);
        ArrayList<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(Command.BUTTON_START,INFO_START));
        botCommands.add(new BotCommand(Command.BUTTON_HELP,INFO_HELP));
        botCommands.add(new BotCommand(Command.USD,INFO_USD));
        botCommands.add(new BotCommand(Command.KZT,INFO_KZT));
        botCommands.add(new BotCommand(Command.CONVERT,INFO_CONVERT));
        telegramBot.execute(new SetMyCommands(botCommands.toArray(new BotCommand[0])));
        return telegramBot;
    }
}
