package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TeamService {
    ResponseEntity<TeamResponse> createTeam(TeamCreateRequest request);
    ResponseEntity<TeamResponse> updateTeam(UUID id, TeamUpdateRequest request);
    ResponseEntity<TeamResponse> getTeam(UUID id);
    ResponseEntity<Void> deleteTeam(UUID id);
    ResponseEntity<Page<TeamResponse>> listTeams(Pageable pageable);
    ResponseEntity<?> addMember(UUID teamId, TeamMemberRequest request);
    ResponseEntity<?> removeMember(UUID teamId, TeamMemberRequest request);
}
