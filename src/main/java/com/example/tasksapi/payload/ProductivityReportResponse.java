package com.example.tasksapi.payload;

import lombok.Data;
import java.util.Map;

@Data
public class ProductivityReportResponse {
    private Map<String, Integer> completedTasksPerUser;
    private Map<String, Integer> overdueTasksPerUser;
    private Map<String, Double> productivityScorePerUser;
}
