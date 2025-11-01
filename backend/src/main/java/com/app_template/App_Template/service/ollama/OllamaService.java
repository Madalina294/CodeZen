package com.app_template.App_Template.service.ollama;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.app_template.App_Template.entity.CustomGuideline;
import com.app_template.App_Template.entity.Project;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * OllamaService - Handles communication with the local Ollama API for code reviews.
 * This service sends code snippets to Ollama and processes the AI-generated feedback.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaService {

    /**
     * Internal DTO for Ollama API request.
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class OllamaRequest {
        private String model;
        private String prompt;
        private Boolean stream;
    }

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${ollama.api.url:http://localhost:11434/api/generate}")
    private String ollamaApiUrl;

    @Value("${ollama.model:codellama:7b}")
    private String ollamaModel;

    private WebClient webClient;

    /**
     * Initialize WebClient if not already initialized.
     */
    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientBuilder.baseUrl("http://localhost:11434").build();
        }
        return webClient;
    }

    /**
     * Generate code review using Ollama.
     *
     * @param code The code snippet to review
     * @param project The project containing custom guidelines
     * @return JSON response as string from Ollama
     */
    public Mono<String> reviewCode(String code, Project project) {
        // Build prompt with guidelines
        String prompt = buildReviewPrompt(code, project);

        // Prepare request body
        OllamaRequest request = OllamaRequest.builder()
                .model(ollamaModel)
                .prompt(prompt)
                .stream(false)
                .build();

        log.info("Sending review request to Ollama for project: {}", project.getId());

        // Make async call to Ollama
        return getWebClient()
                .post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        // Parse Ollama response
                        JsonNode jsonNode = objectMapper.readTree(response);
                        String responseText = jsonNode.path("response").asText();
                        return responseText;
                    } catch (Exception e) {
                        log.error("Error parsing Ollama response", e);
                        return "Error parsing response: " + e.getMessage();
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error calling Ollama API", error);
                    return Mono.just("Error: Unable to connect to Ollama. Please ensure Ollama is running on http://localhost:11434");
                });
    }

    /**
     * Build the review prompt including custom guidelines.
     */
    private String buildReviewPrompt(String code, Project project) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a senior software engineer performing a professional code review.\n");
        prompt.append("Analyze the following code and provide structured feedback.\n\n");

        // Add custom guidelines if they exist
        List<CustomGuideline> guidelines = project.getGuidelines();
        if (guidelines != null && !guidelines.isEmpty()) {
            prompt.append("IMPORTANT PROJECT-SPECIFIC GUIDELINES:\n");
            for (CustomGuideline guideline : guidelines) {
                prompt.append("- ").append(guideline.getRuleText()).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("CODE TO REVIEW:\n");
        prompt.append("```").append(project.getLanguage()).append("\n");
        prompt.append(code);
        prompt.append("\n```\n\n");

        prompt.append("Please provide a comprehensive code review in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"summary\": \"Brief overview of the code quality\",\n");
        prompt.append("  \"findings\": [\n");
        prompt.append("    {\"line\": 42, \"type\": \"bug|style|performance|security\", \"message\": \"Issue description\", \"suggestion\": \"How to fix it\"}\n");
        prompt.append("  ],\n");
        prompt.append("  \"effort_estimation\": \"3/10\"\n");
        prompt.append("}\n\n");
        prompt.append("Review for:\n");
        prompt.append("- Code quality and best practices\n");
        prompt.append("- Performance optimizations\n");
        prompt.append("- Security vulnerabilities\n");
        prompt.append("- Style consistency\n");
        prompt.append("- Potential bugs\n\n");
        prompt.append("Write all comments in English. Return only valid JSON.");

        return prompt.toString();
    }




}