package com.example.tasksapi.controllers;

import com.example.tasksapi.payload.*;
import com.example.tasksapi.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/global")
    public ResponseEntity<?> globalSearch(@RequestBody SearchRequest request, Pageable pageable) {
        return searchService.globalSearch(request, pageable);
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> taskSearch(@RequestBody SearchRequest request, Pageable pageable) {
        return searchService.taskSearch(request, pageable);
    }
}
