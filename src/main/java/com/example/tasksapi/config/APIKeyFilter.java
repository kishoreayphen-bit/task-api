package com.example.tasksapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class APIKeyFilter extends OncePerRequestFilter {
    @Value("${api.key:changeme}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Only protect integration endpoints, e.g. /api/v1/integrations/**
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/integrations")) {
            String key = request.getHeader("X-API-KEY");
            if (key == null || !key.equals(apiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid API Key");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
