package com.example.tasksapi.payload;

import lombok.Data;
import java.util.UUID;

@Data
public class ProjectSummaryResponse {
    private UUID projectId;
    private String projectName;
    private int totalTasks;
    private int completedTasks;
    private int overdueTasks;
    private double progressPercent;
}
