package com.example.tasksapi.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {
    private UUID id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
