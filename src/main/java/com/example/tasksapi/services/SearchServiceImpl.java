package com.example.tasksapi.services;

import com.example.tasksapi.models.Task;
import com.example.tasksapi.models.User;
import com.example.tasksapi.payload.SearchRequest;
import com.example.tasksapi.payload.TaskResponse;
import com.example.tasksapi.repositories.TaskRepository;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private TaskResponse toTaskResponse(Task task) {
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
    public ResponseEntity<?> globalSearch(SearchRequest request, Pageable pageable) {
        // For demonstration: search tasks and users, combine results
        Page<Task> taskPage = taskSearchInternal(request, pageable);
        List<User> users = userRepository.findAll().stream()
            .filter(u -> request.getQuery() != null && (
                u.getUsername().toLowerCase().contains(request.getQuery().toLowerCase()) ||
                (u.getFirstName() != null && u.getFirstName().toLowerCase().contains(request.getQuery().toLowerCase())) ||
                (u.getLastName() != null && u.getLastName().toLowerCase().contains(request.getQuery().toLowerCase()))
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(new GlobalSearchResult(taskPage.getContent().stream().map(this::toTaskResponse).collect(Collectors.toList()), users));
    }

    @Override
    public ResponseEntity<?> taskSearch(SearchRequest request, Pageable pageable) {
        Page<Task> taskPage = taskSearchInternal(request, pageable);
        Page<TaskResponse> resp = taskPage.map(this::toTaskResponse);
        return ResponseEntity.ok(resp);
    }

    private Page<Task> taskSearchInternal(SearchRequest req, Pageable pageable) {
        // For demonstration, fetch all and filter in-memory; replace with JPA Specifications for production
        List<Task> all = taskRepository.findAll();
        List<Task> filtered = all.stream()
            .filter(t -> req.getQuery() == null ||
                (t.getTitle() != null && t.getTitle().toLowerCase().contains(req.getQuery().toLowerCase())) ||
                (t.getDescription() != null && t.getDescription().toLowerCase().contains(req.getQuery().toLowerCase()))
            )
            .filter(t -> req.getStatus() == null || (t.getStatus() != null && t.getStatus().name().equals(req.getStatus())))
            .filter(t -> req.getPriority() == null || (t.getPriority() != null && t.getPriority().name().equals(req.getPriority())))
            .filter(t -> req.getAssigneeId() == null || (t.getAssignee() != null && t.getAssignee().getId().equals(req.getAssigneeId())))
            .filter(t -> req.getProjectId() == null || (t.getProject() != null && t.getProject().getId().equals(req.getProjectId())))
            .filter(t -> req.getTags() == null || req.getTags().isEmpty() || (t.getTags() != null && t.getTags().containsAll(req.getTags())))
            .filter(t -> req.getDueDateFrom() == null || (t.getDueDate() != null && !t.getDueDate().isBefore(req.getDueDateFrom())))
            .filter(t -> req.getDueDateTo() == null || (t.getDueDate() != null && !t.getDueDate().isAfter(req.getDueDateTo())))
            .filter(t -> req.getCreatedFrom() == null || (t.getCreatedAt() != null && !t.getCreatedAt().isBefore(req.getCreatedFrom())))
            .filter(t -> req.getCreatedTo() == null || (t.getCreatedAt() != null && !t.getCreatedAt().isAfter(req.getCreatedTo())))
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<Task> pageContent = start > end ? List.of() : filtered.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    // Helper class for global search result
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class GlobalSearchResult {
        private List<TaskResponse> tasks;
        private List<User> users;
    }
}
