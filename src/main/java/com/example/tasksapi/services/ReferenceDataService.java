package com.example.tasksapi.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceDataService {
    @Cacheable("roles")
    public List<String> getRoles() {
        // Simulate expensive DB call
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        return List.of("ADMIN", "PROJECT_MANAGER", "TEAM_MEMBER", "GUEST");
    }
}
