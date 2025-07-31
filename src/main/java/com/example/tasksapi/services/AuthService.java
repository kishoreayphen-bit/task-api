package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterRequest request);
    ResponseEntity<?> login(LoginRequest request);
    ResponseEntity<?> refreshToken(TokenRefreshRequest request);
    ResponseEntity<?> verifyEmail(String token);
}
