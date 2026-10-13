package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.dto.RequirementRequest;
import com.chaitanya.agenticsdlc.dto.RequirementResponse;
import com.chaitanya.agenticsdlc.service.RequirementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequirementResponse create(@Valid @RequestBody RequirementRequest request) {
        return requirementService.create(request);
    }

    @GetMapping
    public List<RequirementResponse> findAll() {
        return requirementService.findAll();
    }
}