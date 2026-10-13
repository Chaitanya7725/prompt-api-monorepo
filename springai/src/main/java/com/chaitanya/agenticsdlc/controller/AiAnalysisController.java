package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.dto.AiAnalysisResponse;
import com.chaitanya.agenticsdlc.entity.Requirement;
import com.chaitanya.agenticsdlc.service.AiAnalysisService;
import com.chaitanya.agenticsdlc.service.RequirementService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requirements")
public class AiAnalysisController {

    private final RequirementService requirementService;
    private final AiAnalysisService aiAnalysisService;

    public AiAnalysisController(
            RequirementService requirementService,
            AiAnalysisService aiAnalysisService
    ) {
        this.requirementService = requirementService;
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/{id}/analyze")
    public AiAnalysisResponse analyze(@PathVariable UUID id) {
        Requirement requirement = requirementService.findEntityById(id);
        return aiAnalysisService.analyze(requirement);
    }
}