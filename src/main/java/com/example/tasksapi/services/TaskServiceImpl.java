package com.example.tasksapi.services;

import com.example.tasksapi.models.*;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.TaskRepository;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private TaskResponse toResponse(Task task) {
        TaskResponse resp = new TaskResponse();
        resp.setId(task.getId());
        resp.setTitle(task.getTitle());
        resp.setDescription(task.getDescription());
        resp.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        resp.setPriority(task.getPriority() != null ? task.getPriority().name() : null);
        resp.setDueDate(task.getDueDate());
        resp.setEstimatedHours(task.getEstimatedHours());
        resp.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);
        resp.setProjectId(task.getProject() != null ? task.getProject().getId() : null);
        resp.setTags(task.getTags());
        resp.setAttachments(task.getAttachments());
        resp.setCreatedAt(task.getCreatedAt());
        resp.setUpdatedAt(task.getUpdatedAt());
        resp.setParentTaskId(task.getParentTask() != null ? task.getParentTask().getId() : null);
        resp.setDependencyIds(task.getDependencies() != null ?
            task.getDependencies().stream().map(Task::getId).collect(Collectors.toSet()) : null);
        resp.setRecurring(task.isRecurring());
        resp.setRecurrenceRule(task.getRecurrenceRule());
        resp.setTemplate(task.isTemplate());
        return resp;
    }

    @Override
    public ResponseEntity<TaskResponse> createTask(TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? TaskStatus.valueOf(request.getStatus()) : TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? TaskPriority.valueOf(request.getPriority()) : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setEstimatedHours(request.getEstimatedHours());
        if (request.getAssigneeId() != null) {
            userRepository.findById(request.getAssigneeId()).ifPresent(task::setAssignee);
        }
        // TODO: Set project if needed
        task.setTags(request.getTags());
        task.setAttachments(request.getAttachments());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        if (request.getParentTaskId() != null) {
            taskRepository.findById(request.getParentTaskId()).ifPresent(task::setParentTask);
        }
        if (request.getDependencyIds() != null && !request.getDependencyIds().isEmpty()) {
            Set<Task> dependencies = request.getDependencyIds().stream()
                .map(taskRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            task.setDependencies(dependencies);
        }
        task.setRecurring(request.isRecurring());
        task.setRecurrenceRule(request.getRecurrenceRule());
        task.setTemplate(request.isTemplate());
        task = taskRepository.save(task);
        return ResponseEntity.ok(toResponse(task));
    }

    @Override
    public ResponseEntity<TaskResponse> updateTask(UUID id, TaskUpdateRequest request) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Task task = opt.get();
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(TaskStatus.valueOf(request.getStatus()));
        if (request.getPriority() != null) task.setPriority(TaskPriority.valueOf(request.getPriority()));
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
        if (request.getAssigneeId() != null) userRepository.findById(request.getAssigneeId()).ifPresent(task::setAssignee);
        if (request.getProjectId() != null) {
            // TODO: Set project if needed
        }
        if (request.getTags() != null) task.setTags(request.getTags());
        if (request.getAttachments() != null) task.setAttachments(request.getAttachments());
        if (request.getParentTaskId() != null) taskRepository.findById(request.getParentTaskId()).ifPresent(task::setParentTask);
        if (request.getDependencyIds() != null) {
            Set<Task> dependencies = request.getDependencyIds().stream()
                .map(taskRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            task.setDependencies(dependencies);
        }
        task.setRecurring(request.isRecurring());
        task.setRecurrenceRule(request.getRecurrenceRule());
        task.setTemplate(request.isTemplate());
        task.setUpdatedAt(LocalDateTime.now());
        task = taskRepository.save(task);
        return ResponseEntity.ok(toResponse(task));
    }

    @Override
    public ResponseEntity<TaskResponse> getTask(UUID id) {
        Optional<Task> opt = taskRepository.findById(id);
        return opt.map(task -> ResponseEntity.ok(toResponse(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) return ResponseEntity.notFound().build();
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<TaskResponse>> listTasks(String filter, Pageable pageable, String sort) {
        // Basic implementation: fetch all and filter in-memory (replace with JPA Specifications for production)
        Page<Task> page = taskRepository.findAll(pageable);
        Page<TaskResponse> resp = page.map(this::toResponse);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> bulkOperation(TaskBulkOperationRequest request) {
        if (request.getOperation() == null) return ResponseEntity.badRequest().body("No operation specified");
        List<Task> tasks = taskRepository.findAllById(request.getTaskIds());
        switch (request.getOperation()) {
            case "DELETE":
                taskRepository.deleteAll(tasks);
                return ResponseEntity.ok("Bulk delete successful");
            case "UPDATE_STATUS":
                if (request.getStatus() == null) return ResponseEntity.badRequest().body("No status specified");
                for (Task t : tasks) {
                    t.setStatus(TaskStatus.valueOf(request.getStatus()));
                    t.setUpdatedAt(LocalDateTime.now());
                }
                taskRepository.saveAll(tasks);
                return ResponseEntity.ok("Bulk status update successful");
            default:
                return ResponseEntity.badRequest().body("Unknown operation");
        }
    }
}
