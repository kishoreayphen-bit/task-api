package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProjectService {
    ResponseEntity<ProjectResponse> createProject(ProjectCreateRequest request);
    ResponseEntity<ProjectResponse> updateProject(UUID id, ProjectUpdateRequest request);
    ResponseEntity<ProjectResponse> getProject(UUID id);
    ResponseEntity<Void> deleteProject(UUID id);
    ResponseEntity<Page<ProjectResponse>> listProjects(Pageable pageable);
    ResponseEntity<?> addMember(UUID projectId, ProjectMemberRequest request);
    ResponseEntity<?> removeMember(UUID projectId, ProjectMemberRequest request);
}
