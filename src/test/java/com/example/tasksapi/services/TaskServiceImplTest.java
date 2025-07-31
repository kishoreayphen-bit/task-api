package com.example.tasksapi.services;

import com.example.tasksapi.models.Task;
import com.example.tasksapi.payload.TaskCreateRequest;
import com.example.tasksapi.payload.TaskResponse;
import com.example.tasksapi.repositories.TaskRepository;
import com.example.tasksapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testCreateTask() {
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("Test Task");
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Test Task");
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        ResponseEntity<TaskResponse> resp = taskService.createTask(req);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Test Task", resp.getBody().getTitle());
    }
}
