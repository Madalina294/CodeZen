package com.app_template.App_Template.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for review response data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private LocalDateTime timestamp;
    private String codeSnapshot;
    private String llmResponse;
    private String effortEstimation;
    private Long projectId;
    private Long userId;
}