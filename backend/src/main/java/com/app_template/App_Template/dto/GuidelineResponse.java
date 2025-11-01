package com.app_template.App_Template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for guideline response data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuidelineResponse {
    private Long id;
    private String ruleText;
    private Long projectId;
}