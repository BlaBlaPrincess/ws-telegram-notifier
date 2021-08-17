package com.github.blablaprincess.telegramnotifier.service;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelegramNotifierServiceUT {

    private TelegramNotifierService telegramBotNotifierService;
    private static MockWebServer webServer;

    @BeforeAll
    static void setup() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @BeforeEach
    void init() {
        String baseUrl = String.format("http://localhost:%s?text=", webServer.getPort());
        telegramBotNotifierService = new TelegramNotifierService(baseUrl, WebClient.create(baseUrl));
    }

    @AfterAll
    static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @Test
    void sendNotification() throws InterruptedException {
        // Arrange
        String message = "123abc\n!@#$%^&*()-+=\n\uD83D\uDC35\uD83D\uDE48\uD83D\uDE49️";
        webServer.enqueue(new MockResponse().setResponseCode(200));

        // Act
        telegramBotNotifierService.sendNotification(message);
        RecordedRequest recordedRequest = webServer.takeRequest();
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        String sent = requestUrl.queryParameter("text");

        // Assert
        assertEquals(message, sent);
    }

}