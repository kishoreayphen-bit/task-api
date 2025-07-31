package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface NotificationService {
    ResponseEntity<?> listNotifications();
    ResponseEntity<?> markRead(UUID notificationId);
    ResponseEntity<?> getPreferences();
    ResponseEntity<?> updatePreferences(NotificationPreferences preferences);
}
