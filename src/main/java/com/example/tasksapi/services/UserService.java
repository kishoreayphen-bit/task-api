package com.example.tasksapi.services;

import com.example.tasksapi.payload.*;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public interface UserService {
    ResponseEntity<?> getCurrentUserProfile();
    ResponseEntity<?> updateCurrentUserProfile(UpdateProfileRequest request);
    ResponseEntity<?> deleteUser(UUID id);
    ResponseEntity<?> changePassword(ChangePasswordRequest request);
    ResponseEntity<?> resetPassword(ResetPasswordRequest request);
}
