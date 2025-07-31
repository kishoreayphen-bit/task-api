package com.example.tasksapi.services;

import com.example.tasksapi.models.*;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.CommentRepository;
import com.example.tasksapi.repositories.TaskRepository;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse resp = new CommentResponse();
        resp.setId(comment.getId());
        resp.setTaskId(comment.getTask().getId());
        resp.setUserId(comment.getUser().getId());
        resp.setContent(comment.getContent());
        resp.setCreatedAt(comment.getCreatedAt());
        resp.setUpdatedAt(comment.getUpdatedAt());
        return resp;
    }

    @Override
    public ResponseEntity<CommentResponse> createComment(CommentCreateRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(request.getTaskId());
        if (taskOpt.isEmpty()) return ResponseEntity.badRequest().build();
        User user = getCurrentUser();
        Comment comment = new Comment();
        comment.setTask(taskOpt.get());
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return ResponseEntity.ok(toResponse(comment));
    }

    @Override
    public ResponseEntity<CommentResponse> updateComment(UUID id, CommentUpdateRequest request) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Comment comment = opt.get();
        User user = getCurrentUser();
        if (!comment.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return ResponseEntity.ok(toResponse(comment));
    }

    @Override
    public ResponseEntity<CommentResponse> getComment(UUID id) {
        Optional<Comment> opt = commentRepository.findById(id);
        return opt.map(comment -> ResponseEntity.ok(toResponse(comment)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteComment(UUID id) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Comment comment = opt.get();
        User user = getCurrentUser();
        if (!comment.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        commentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<CommentResponse>> listComments(UUID taskId, Pageable pageable) {
        Page<Comment> page = commentRepository.findByTaskId(taskId, pageable);
        Page<CommentResponse> resp = page.map(this::toResponse);
        return ResponseEntity.ok(resp);
    }
}
