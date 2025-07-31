package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.createProject(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.updateProject(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        return projectService.deleteProject(id);
    }

    @GetMapping
    public ResponseEntity<?> listProjects(Pageable pageable) {
        return projectService.listProjects(pageable);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMember(@PathVariable UUID id, @RequestBody ProjectMemberRequest request) {
        return projectService.addMember(id, request);
    }

    @DeleteMapping("/{id}/members")
    public ResponseEntity<?> removeMember(@PathVariable UUID id, @RequestBody ProjectMemberRequest request) {
        return projectService.removeMember(id, request);
    }
}
