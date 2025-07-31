package com.example.tasksapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private static final long ALLOWED_REQUESTS_PER_MINUTE = 60;
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, k -> new RequestCounter());
        synchronized (counter) {
            if (currentTime - counter.timestamp > 60000) {
                counter.timestamp = currentTime;
                counter.count = 0;
            }
            counter.count++;
            if (counter.count > ALLOWED_REQUESTS_PER_MINUTE) {
                response.setStatus(429);
                response.getWriter().write("Too Many Requests");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private static class RequestCounter {
        long timestamp = System.currentTimeMillis();
        long count = 0;
    }
}
