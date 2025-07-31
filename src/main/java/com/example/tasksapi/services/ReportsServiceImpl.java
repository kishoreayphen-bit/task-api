package com.example.tasksapi.services;

import com.example.tasksapi.models.Project;
import com.example.tasksapi.models.Task;
import com.example.tasksapi.models.TaskStatus;
import com.example.tasksapi.models.User;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.ProjectRepository;
import com.example.tasksapi.repositories.TaskRepository;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<ProductivityReportResponse> getProductivityReport() {
        List<User> users = userRepository.findAll();
        List<Task> tasks = taskRepository.findAll();
        Map<String, Integer> completed = new HashMap<>();
        Map<String, Integer> overdue = new HashMap<>();
        Map<String, Double> score = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (User user : users) {
            int completedCount = (int) tasks.stream()
                    .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(user.getId()))
                    .filter(t -> t.getStatus() == TaskStatus.DONE)
                    .count();
            int overdueCount = (int) tasks.stream()
                    .filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(user.getId()))
                    .filter(t -> t.getStatus() != TaskStatus.DONE && t.getDueDate() != null && t.getDueDate().toLocalDate().isBefore(today))
                    .count();
            completed.put(user.getUsername(), completedCount);
            overdue.put(user.getUsername(), overdueCount);
            score.put(user.getUsername(), completedCount / (double) (completedCount + overdueCount + 1));
        }
        ProductivityReportResponse resp = new ProductivityReportResponse();
        resp.setCompletedTasksPerUser(completed);
        resp.setOverdueTasksPerUser(overdue);
        resp.setProductivityScorePerUser(score);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<ProjectSummaryResponse> getProjectSummary(UUID projectId) {
        Optional<Project> projOpt = projectRepository.findById(projectId);
        if (projOpt.isEmpty()) return ResponseEntity.notFound().build();
        Project project = projOpt.get();
        List<Task> tasks = taskRepository.findAll().stream()
                .filter(t -> t.getProject() != null && t.getProject().getId().equals(projectId))
                .collect(Collectors.toList());
        int total = tasks.size();
        int completed = (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        int overdue = (int) tasks.stream().filter(t -> t.getStatus() != TaskStatus.DONE && t.getDueDate() != null && t.getDueDate().isBefore(LocalDateTime.now())).count();
        double progress = total == 0 ? 0.0 : (completed * 100.0 / total);
        ProjectSummaryResponse resp = new ProjectSummaryResponse();
        resp.setProjectId(projectId);
        resp.setProjectName(project.getName());
        resp.setTotalTasks(total);
        resp.setCompletedTasks(completed);
        resp.setOverdueTasks(overdue);
        resp.setProgressPercent(progress);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<OverdueTasksResponse> getOverdueTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<UUID> overdue = tasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.DONE && t.getDueDate() != null && t.getDueDate().isBefore(LocalDateTime.now()))
                .map(Task::getId).collect(Collectors.toList());
        OverdueTasksResponse resp = new OverdueTasksResponse();
        resp.setOverdueTaskIds(overdue);
        resp.setOverdueCount(overdue.size());
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<TeamWorkloadResponse> getTeamWorkload() {
        List<User> users = userRepository.findAll();
        List<Task> tasks = taskRepository.findAll();
        Map<String, Integer> assigned = new HashMap<>();
        Map<String, Integer> inProgress = new HashMap<>();
        Map<String, Integer> completed = new HashMap<>();
        for (User user : users) {
            assigned.put(user.getUsername(), (int) tasks.stream().filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(user.getId())).count());
            inProgress.put(user.getUsername(), (int) tasks.stream().filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(user.getId()) && t.getStatus() == TaskStatus.IN_PROGRESS).count());
            completed.put(user.getUsername(), (int) tasks.stream().filter(t -> t.getAssignee() != null && t.getAssignee().getId().equals(user.getId()) && t.getStatus() == TaskStatus.DONE).count());
        }
        TeamWorkloadResponse resp = new TeamWorkloadResponse();
        resp.setTasksAssigned(assigned);
        resp.setTasksInProgress(inProgress);
        resp.setTasksCompleted(completed);
        return ResponseEntity.ok(resp);
    }
}
