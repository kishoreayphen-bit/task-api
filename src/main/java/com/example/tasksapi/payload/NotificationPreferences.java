package com.example.tasksapi.payload;

import lombok.Data;

@Data
public class NotificationPreferences {
    private boolean emailEnabled;
    private boolean pushEnabled;
}
