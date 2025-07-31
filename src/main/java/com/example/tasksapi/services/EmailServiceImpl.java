package com.example.tasksapi.services;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendVerificationEmail(String to, String verificationToken) {
        // TODO: Implement actual email sending logic
        System.out.println("Verification email sent to " + to + ", token: " + verificationToken);
    }
}
