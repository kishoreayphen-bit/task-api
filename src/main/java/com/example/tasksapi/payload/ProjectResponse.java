package com.example.tasksapi.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class ProjectResponse {
    private UUID id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID ownerId;
    private Set<UUID> teamIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
