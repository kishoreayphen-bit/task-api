package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportsController {
    private final ReportsService reportsService;

    @GetMapping("/productivity")
    public ResponseEntity<ProductivityReportResponse> productivityReport() {
        return reportsService.getProductivityReport();
    }

    @GetMapping("/project-summary/{projectId}")
    public ResponseEntity<ProjectSummaryResponse> projectSummary(@PathVariable UUID projectId) {
        return reportsService.getProjectSummary(projectId);
    }

    @GetMapping("/overdue-tasks")
    public ResponseEntity<OverdueTasksResponse> overdueTasks() {
        return reportsService.getOverdueTasks();
    }

    @GetMapping("/team-workload")
    public ResponseEntity<TeamWorkloadResponse> teamWorkload() {
        return reportsService.getTeamWorkload();
    }
}
