package com.example.tasksapi.payload;

import lombok.Data;
import java.util.UUID;

@Data
public class CommentCreateRequest {
    private UUID taskId;
    private String content;
}
