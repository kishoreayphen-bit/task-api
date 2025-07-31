package com.example.tasksapi.services;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationToken);
}
