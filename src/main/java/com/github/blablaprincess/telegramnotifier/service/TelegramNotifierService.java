package com.github.blablaprincess.telegramnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotifierService implements TelegramNotifier {

    @Value("https://api.telegram.org/bot${telegram-bot.http-token}/sendMessage?chat_id=${telegram-bot.chat-id}&text=")
    private final String messageHttpRequestTemplate;
    private final WebClient client;

    @Override
    @SneakyThrows
    public void sendNotification(String message) {
        String utf8message = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
        URI uri = URI.create(messageHttpRequestTemplate + utf8message);

        client.get()
                .uri(uri)
                .retrieve()
                .toBodilessEntity()
                .doOnError(this::logNotificationFailed)
                .block();
    }

    private void logNotificationFailed(Throwable throwable) {
        log.error("Telegram notification failed.", throwable);
    }

}
