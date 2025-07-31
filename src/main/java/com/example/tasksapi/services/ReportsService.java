package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ReportsService {
    ResponseEntity<ProductivityReportResponse> getProductivityReport();
    ResponseEntity<ProjectSummaryResponse> getProjectSummary(UUID projectId);
    ResponseEntity<OverdueTasksResponse> getOverdueTasks();
    ResponseEntity<TeamWorkloadResponse> getTeamWorkload();
}
