package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.dto.BuildAssistantRequest;
import com.chaitanya.agenticsdlc.service.AiAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai/build")
public class BuildAssistantController {

    private final AiAnalysisService aiAnalysisService;

    public BuildAssistantController(AiAnalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping(
            value = "/chat",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> chat(
            @Valid @RequestBody BuildAssistantRequest request
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(aiAnalysisService.askBuildAssistant(request.message()));
    }
}