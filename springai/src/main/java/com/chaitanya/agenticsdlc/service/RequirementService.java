package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.dto.RequirementRequest;
import com.chaitanya.agenticsdlc.dto.RequirementResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import com.chaitanya.agenticsdlc.dto.RequirementRequest;
import com.chaitanya.agenticsdlc.dto.RequirementResponse;
import com.chaitanya.agenticsdlc.entity.Requirement;
import com.chaitanya.agenticsdlc.repository.RequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    public RequirementResponse create(RequirementRequest request) {
        Requirement requirement = new Requirement(
                request.title(),
                request.description()
        );

        Requirement saved = requirementRepository.save(requirement);
        return toResponse(saved);
    }

    public List<RequirementResponse> findAll() {
        return requirementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Requirement findEntityById(UUID id) {
        return requirementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Requirement not found: " + id
                ));
    }

    private RequirementResponse toResponse(Requirement requirement) {
        return new RequirementResponse(
                requirement.getId(),
                requirement.getTitle(),
                requirement.getDescription(),
                requirement.getStatus().name(),
                requirement.getCreatedAt()
        );
    }
}