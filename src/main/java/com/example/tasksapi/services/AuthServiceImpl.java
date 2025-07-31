package com.example.tasksapi.services;

import com.example.tasksapi.models.User;
import com.example.tasksapi.payload.*;
import com.example.tasksapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        // Check for existing email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        // Check for existing username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already in use");
        }
        // Enforce password strength
        String password = request.getPassword();
        if (!isPasswordStrong(password)) {
            return ResponseEntity.badRequest().body("Password does not meet strength requirements");
        }
        // Hash password
        String encodedPassword = passwordEncoder.encode(password);
        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(encodedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role("USER")
                .emailVerified(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .status("active")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        // Generate verification token
        String token = java.util.UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        verificationTokenRepository.save(verificationToken);
        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token);
        return ResponseEntity.ok("Registration successful. Please check your email to verify your account.");
    }

    private boolean isPasswordStrong(String password) {
        // At least 8 chars, uppercase, lowercase, digit, special char
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        var userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        var user = userOpt.get();
        if (!user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Email not verified");
        }
        if (user.isAccountLocked()) {
            return ResponseEntity.status(423).body("Account is locked due to too many failed login attempts");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        // Reset failed attempts
        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        // Issue JWT
        String jwt = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        // Issue refresh token
        refreshTokenRepository.deleteByUser(user); // Only one active refresh token per user
        String refreshTokenStr = java.util.UUID.randomUUID().toString();
        var refreshToken = com.example.tasksapi.models.RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);
        return ResponseEntity.ok(new LoginResponse(jwt, refreshTokenStr));
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        var tokenOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
        var refreshToken = tokenOpt.get();
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            return ResponseEntity.badRequest().body("Refresh token expired");
        }
        var user = refreshToken.getUser();
        String jwt = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @Override
    public ResponseEntity<?> verifyEmail(String token) {
        var optToken = verificationTokenRepository.findByToken(token);
        if (optToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or expired verification token");
        }
        var verificationToken = optToken.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Verification token has expired");
        }
        var user = verificationToken.getUser();
        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Email already verified");
        }
        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }
}

