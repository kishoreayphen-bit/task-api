package com.example.tasksapi.services;

import com.example.tasksapi.payload.SearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SearchService {
    ResponseEntity<?> globalSearch(SearchRequest request, Pageable pageable);
    ResponseEntity<?> taskSearch(SearchRequest request, Pageable pageable);
}
