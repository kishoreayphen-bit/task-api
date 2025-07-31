package com.example.tasksapi.payload;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class TeamResponse {
    private UUID id;
    private String name;
    private String description;
    private Set<UUID> memberIds;
}
