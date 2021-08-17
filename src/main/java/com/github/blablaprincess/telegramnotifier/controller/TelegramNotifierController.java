package com.github.blablaprincess.telegramnotifier.controller;

import com.github.blablaprincess.telegramnotifier.service.TelegramNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramNotifierController {

    private final TelegramNotifier notifier;

    @PostMapping("/{message}")
    public void sendNotification(@PathVariable String message) {
        notifier.sendNotification(message);
    }

}
