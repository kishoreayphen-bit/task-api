package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        return taskService.deleteTask(id);
    }

    @GetMapping
    public ResponseEntity<?> listTasks(@RequestParam(required = false) String filter, Pageable pageable, @RequestParam(required = false) String sort) {
        return taskService.listTasks(filter, pageable, sort);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkOperation(@RequestBody TaskBulkOperationRequest request) {
        return taskService.bulkOperation(request);
    }
}
