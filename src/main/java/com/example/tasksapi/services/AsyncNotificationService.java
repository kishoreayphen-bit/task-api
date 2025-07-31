package com.example.tasksapi.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncNotificationService {
    @Async
    public void sendNotificationAsync(String recipient, String message) {
        log.info("Sending notification to {}: {}", recipient, message);
        // Simulate delay
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        log.info("Notification sent to {}", recipient);
    }
}
