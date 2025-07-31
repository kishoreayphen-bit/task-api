package com.example.tasksapi.payload;

import lombok.Data;
import java.util.UUID;

@Data
public class TeamMemberRequest {
    private UUID userId;
}
