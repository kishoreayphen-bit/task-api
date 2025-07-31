package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CommentService {
    ResponseEntity<CommentResponse> createComment(CommentCreateRequest request);
    ResponseEntity<CommentResponse> updateComment(UUID id, CommentUpdateRequest request);
    ResponseEntity<CommentResponse> getComment(UUID id);
    ResponseEntity<Void> deleteComment(UUID id);
    ResponseEntity<Page<CommentResponse>> listComments(UUID taskId, Pageable pageable);
}
