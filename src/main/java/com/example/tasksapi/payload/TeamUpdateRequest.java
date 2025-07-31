package com.example.tasksapi.payload;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class TeamUpdateRequest {
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String description;
    private String name;
    private String description;
    private Set<UUID> memberIds;
}
