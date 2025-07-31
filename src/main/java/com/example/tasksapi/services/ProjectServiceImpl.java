package com.example.tasksapi.services;

import com.example.tasksapi.models.*;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.ProjectRepository;
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
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private ProjectResponse toResponse(Project project) {
        ProjectResponse resp = new ProjectResponse();
        resp.setId(project.getId());
        resp.setName(project.getName());
        resp.setDescription(project.getDescription());
        resp.setStatus(project.getStatus() != null ? project.getStatus().name() : null);
        resp.setStartDate(project.getStartDate());
        resp.setEndDate(project.getEndDate());
        resp.setOwnerId(project.getOwner() != null ? project.getOwner().getId() : null);
        resp.setTeamIds(project.getTeam() != null ?
            project.getTeam().stream().map(User::getId).collect(Collectors.toSet()) : null);
        resp.setCreatedAt(project.getCreatedAt());
        resp.setUpdatedAt(project.getUpdatedAt());
        return resp;
    }

    @Override
    public ResponseEntity<ProjectResponse> createProject(ProjectCreateRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? ProjectStatus.valueOf(request.getStatus()) : ProjectStatus.PLANNING);
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        if (request.getOwnerId() != null) {
            userRepository.findById(request.getOwnerId()).ifPresent(project::setOwner);
        }
        if (request.getTeamIds() != null && !request.getTeamIds().isEmpty()) {
            Set<User> team = request.getTeamIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            project.setTeam(team);
        }
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project = projectRepository.save(project);
        return ResponseEntity.ok(toResponse(project));
    }

    @Override
    public ResponseEntity<ProjectResponse> updateProject(UUID id, ProjectUpdateRequest request) {
        Optional<Project> opt = projectRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Project project = opt.get();
        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(ProjectStatus.valueOf(request.getStatus()));
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());
        if (request.getOwnerId() != null) userRepository.findById(request.getOwnerId()).ifPresent(project::setOwner);
        if (request.getTeamIds() != null) {
            Set<User> team = request.getTeamIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            project.setTeam(team);
        }
        project.setUpdatedAt(LocalDateTime.now());
        project = projectRepository.save(project);
        return ResponseEntity.ok(toResponse(project));
    }

    @Override
    public ResponseEntity<ProjectResponse> getProject(UUID id) {
        Optional<Project> opt = projectRepository.findById(id);
        return opt.map(project -> ResponseEntity.ok(toResponse(project)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) return ResponseEntity.notFound().build();
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<ProjectResponse>> listProjects(Pageable pageable) {
        Page<Project> page = projectRepository.findAll(pageable);
        Page<ProjectResponse> resp = page.map(this::toResponse);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> addMember(UUID projectId, ProjectMemberRequest request) {
        Optional<Project> opt = projectRepository.findById(projectId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Project project = opt.get();
        if (request.getUserId() == null) return ResponseEntity.badRequest().body("No userId specified");
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        project.getTeam().add(userOpt.get());
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
        return ResponseEntity.ok("User added to project");
    }

    @Override
    public ResponseEntity<?> removeMember(UUID projectId, ProjectMemberRequest request) {
        Optional<Project> opt = projectRepository.findById(projectId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Project project = opt.get();
        if (request.getUserId() == null) return ResponseEntity.badRequest().body("No userId specified");
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        project.getTeam().remove(userOpt.get());
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
        return ResponseEntity.ok("User removed from project");
    }
}
