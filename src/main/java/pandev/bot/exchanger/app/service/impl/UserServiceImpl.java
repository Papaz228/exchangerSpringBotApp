package pandev.bot.exchanger.app.service.impl;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pandev.bot.exchanger.app.service.UserService;
import pandev.bot.exchanger.app.model.User;
import pandev.bot.exchanger.app.model.UserMessage;
import pandev.bot.exchanger.app.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void registerUser(Message message){
        Chat chat = message.chat();
        User newUser = User.builder().chatId(chat.id()).firstName(chat.firstName()).lastName(chat.lastName()).userName(chat.username()).registeredTime(new Timestamp(System.currentTimeMillis())).messages(new ArrayList<>()).build();
        userRepository.save(newUser);
    }
    @Override
    public Boolean checkUser(Long chatId){
        return userRepository.existsByChatId(chatId);
    }
    @Override
    public void updateUser(Message message) {
        Chat chat = message.chat();
        Long chatId = chat.id();
        User currentUser = userRepository.findByChatId(chatId);

        if (currentUser == null) {
            registerUser(message);
            return;
        }
        UserMessage userMessage;
        if (message.text() == null) {
            userMessage = UserMessage.builder()
                    .user(currentUser)
                    .messageType("Voice")
                    .messageValue(message.voice().fileId())
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            userMessage = UserMessage.builder()
                    .user(currentUser)
                    .messageType("Text")
                    .messageValue(message.text())
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        currentUser.addMessage(userMessage);
        userRepository.save(currentUser);
    }

}
