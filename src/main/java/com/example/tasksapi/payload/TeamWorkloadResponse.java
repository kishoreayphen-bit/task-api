package com.example.tasksapi.payload;

import lombok.Data;
import java.util.Map;

@Data
public class TeamWorkloadResponse {
    private Map<String, Integer> tasksAssigned;
    private Map<String, Integer> tasksInProgress;
    private Map<String, Integer> tasksCompleted;
}
