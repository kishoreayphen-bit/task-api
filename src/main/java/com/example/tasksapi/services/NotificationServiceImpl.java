package com.example.tasksapi.services;

import com.example.tasksapi.models.Notification;
import com.example.tasksapi.models.User;
import com.example.tasksapi.payload.NotificationPreferences;
import com.example.tasksapi.payload.NotificationResponse;
import com.example.tasksapi.repositories.NotificationRepository;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    // For demo: store preferences in memory per user
    private final java.util.Map<UUID, NotificationPreferences> preferencesStore = new java.util.HashMap<>();

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public ResponseEntity<?> listNotifications() {
        User user = getCurrentUser();
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<NotificationResponse> responses = notifications.stream().map(n -> {
            NotificationResponse r = new NotificationResponse();
            r.setId(n.getId());
            r.setMessage(n.getMessage());
            r.setRead(n.isRead());
            r.setCreatedAt(n.getCreatedAt());
            return r;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<?> markRead(UUID notificationId) {
        User user = getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElse(null);
        if (notification == null) return ResponseEntity.notFound().build();
        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok("Notification marked as read");
    }

    @Override
    public ResponseEntity<?> getPreferences() {
        User user = getCurrentUser();
        NotificationPreferences prefs = preferencesStore.getOrDefault(user.getId(), new NotificationPreferences());
        return ResponseEntity.ok(prefs);
    }

    @Override
    public ResponseEntity<?> updatePreferences(NotificationPreferences preferences) {
        User user = getCurrentUser();
        preferencesStore.put(user.getId(), preferences);
        return ResponseEntity.ok("Preferences updated");
    }
}
