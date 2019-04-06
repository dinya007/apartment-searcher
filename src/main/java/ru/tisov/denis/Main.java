package ru.tisov.denis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@SpringBootApplication
@EnableScheduling
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @Bean
    public SearchNewApartmentsTask searchNewApartmentsTask() {
        return new SearchNewApartmentsTask(bot());
    }

    @Bean
    public Bot bot() {
        return new Bot(new DefaultBotOptions());
    }

}
