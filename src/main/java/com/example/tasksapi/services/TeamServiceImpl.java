package com.example.tasksapi.services;

import com.example.tasksapi.models.Team;
import com.example.tasksapi.models.User;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.TeamRepository;
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
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private TeamResponse toResponse(Team team) {
        TeamResponse resp = new TeamResponse();
        resp.setId(team.getId());
        resp.setName(team.getName());
        resp.setDescription(team.getDescription());
        resp.setMemberIds(team.getMembers() != null ?
            team.getMembers().stream().map(User::getId).collect(Collectors.toSet()) : null);
        return resp;
    }

    @Override
    public ResponseEntity<TeamResponse> createTeam(TeamCreateRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            Set<User> members = request.getMemberIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            team.setMembers(members);
        }
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        team = teamRepository.save(team);
        return ResponseEntity.ok(toResponse(team));
    }

    @Override
    public ResponseEntity<TeamResponse> updateTeam(UUID id, TeamUpdateRequest request) {
        Optional<Team> opt = teamRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Team team = opt.get();
        if (request.getName() != null) team.setName(request.getName());
        if (request.getDescription() != null) team.setDescription(request.getDescription());
        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            team.setMembers(members);
        }
        team.setUpdatedAt(LocalDateTime.now());
        team = teamRepository.save(team);
        return ResponseEntity.ok(toResponse(team));
    }

    @Override
    public ResponseEntity<TeamResponse> getTeam(UUID id) {
        Optional<Team> opt = teamRepository.findById(id);
        return opt.map(team -> ResponseEntity.ok(toResponse(team)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteTeam(UUID id) {
        if (!teamRepository.existsById(id)) return ResponseEntity.notFound().build();
        teamRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<TeamResponse>> listTeams(Pageable pageable) {
        Page<Team> page = teamRepository.findAll(pageable);
        Page<TeamResponse> resp = page.map(this::toResponse);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> addMember(UUID teamId, TeamMemberRequest request) {
        Optional<Team> opt = teamRepository.findById(teamId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Team team = opt.get();
        if (request.getUserId() == null) return ResponseEntity.badRequest().body("No userId specified");
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        team.getMembers().add(userOpt.get());
        team.setUpdatedAt(LocalDateTime.now());
        teamRepository.save(team);
        return ResponseEntity.ok("User added to team");
    }

    @Override
    public ResponseEntity<?> removeMember(UUID teamId, TeamMemberRequest request) {
        Optional<Team> opt = teamRepository.findById(teamId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Team team = opt.get();
        if (request.getUserId() == null) return ResponseEntity.badRequest().body("No userId specified");
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        team.getMembers().remove(userOpt.get());
        team.setUpdatedAt(LocalDateTime.now());
        teamRepository.save(team);
        return ResponseEntity.ok("User removed from team");
    }
}
