package pandev.bot.exchanger.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_message")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageType;
    private String messageValue;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private User user;
}
