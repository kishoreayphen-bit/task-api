package com.example.tasksapi.payload;

import lombok.Data;
import java.util.UUID;

@Data
public class CommentUpdateRequest {
    private UUID commentId;
    private String content;
}
