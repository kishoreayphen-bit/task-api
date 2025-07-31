package com.example.tasksapi.payload;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class OverdueTasksResponse {
    private List<UUID> overdueTaskIds;
    private int overdueCount;
}
