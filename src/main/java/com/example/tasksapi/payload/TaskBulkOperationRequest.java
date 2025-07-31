package com.example.tasksapi.payload;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class TaskBulkOperationRequest {
    private List<UUID> taskIds;
    private String operation; // e.g., DELETE, UPDATE_STATUS
    private String status; // for bulk status update
}
