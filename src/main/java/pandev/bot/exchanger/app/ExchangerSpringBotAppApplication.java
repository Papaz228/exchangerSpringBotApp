package pandev.bot.exchanger.app;

import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExchangerSpringBotAppApplication {

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient();
	}

	public static void main(String[] args) {
		SpringApplication.run(ExchangerSpringBotAppApplication.class, args);
	}

}
