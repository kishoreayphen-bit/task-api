package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> listNotifications() {
        return notificationService.listNotifications();
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<?> markRead(@PathVariable UUID id) {
        return notificationService.markRead(id);
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> getPreferences() {
        return notificationService.getPreferences();
    }

    @PutMapping("/preferences")
    public ResponseEntity<?> updatePreferences(@Valid @RequestBody NotificationPreferences preferences) {
        return notificationService.updatePreferences(preferences);
    }
}
