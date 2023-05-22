package pandev.bot.exchanger.app.service;

import com.pengrad.telegrambot.model.Message;

public interface UserService {
    void registerUser(Message message);
    Boolean checkUser(Long chatId);
    void updateUser(Message message);
}
