package pandev.bot.exchanger.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandev.bot.exchanger.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByChatId(Long chatId);

    User findByChatId(Long id);
}
