package com.app_template.App_Template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new comment (user question).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
    private String message;
}

