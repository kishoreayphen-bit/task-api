package com.example.tasksapi.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class TaskCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private Double estimatedHours;
    private UUID assigneeId;
    private UUID projectId;
    private Set<String> tags;
    private Set<String> attachments;
    private UUID parentTaskId;
    private Set<UUID> dependencyIds;
    private boolean recurring;
    private String recurrenceRule;
    private boolean isTemplate;
}
