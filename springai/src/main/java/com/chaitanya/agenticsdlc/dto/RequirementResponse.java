package com.chaitanya.agenticsdlc.dto;

import java.time.Instant;
import java.util.UUID;

public record RequirementResponse(
        UUID id,
        String title,
        String description,
        String status,
        Instant createdAt
) {
}

