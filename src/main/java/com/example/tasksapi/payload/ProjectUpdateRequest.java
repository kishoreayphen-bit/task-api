package com.example.tasksapi.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class ProjectUpdateRequest {
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String description;
    private String name;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID ownerId;
    private Set<UUID> teamIds;
}
