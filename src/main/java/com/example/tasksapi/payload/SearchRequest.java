package com.example.tasksapi.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class SearchRequest {
    private String query; // full-text
    private String status;
    private String priority;
    private UUID assigneeId;
    private UUID projectId;
    private UUID teamId;
    private Set<String> tags;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
}
