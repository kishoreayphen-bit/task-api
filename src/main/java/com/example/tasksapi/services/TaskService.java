package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TaskService {
    ResponseEntity<TaskResponse> createTask(TaskCreateRequest request);
    ResponseEntity<TaskResponse> updateTask(UUID id, TaskUpdateRequest request);
    ResponseEntity<TaskResponse> getTask(UUID id);
    ResponseEntity<Void> deleteTask(UUID id);
    ResponseEntity<Page<TaskResponse>> listTasks(String filter, Pageable pageable, String sort);
    ResponseEntity<?> bulkOperation(TaskBulkOperationRequest request);
}
