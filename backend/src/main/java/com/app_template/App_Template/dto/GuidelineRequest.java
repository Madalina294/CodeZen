package com.app_template.App_Template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for adding custom guidelines to a project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuidelineRequest {
    private String ruleText;
}