package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamCreateRequest request) {
        return teamService.createTeam(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable UUID id, @Valid @RequestBody TeamUpdateRequest request) {
        return teamService.updateTeam(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable UUID id) {
        return teamService.getTeam(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        return teamService.deleteTeam(id);
    }

    @GetMapping
    public ResponseEntity<?> listTeams(Pageable pageable) {
        return teamService.listTeams(pageable);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMember(@PathVariable UUID id, @RequestBody TeamMemberRequest request) {
        return teamService.addMember(id, request);
    }

    @DeleteMapping("/{id}/members")
    public ResponseEntity<?> removeMember(@PathVariable UUID id, @RequestBody TeamMemberRequest request) {
        return teamService.removeMember(id, request);
    }
}
