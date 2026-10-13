package com.chaitanya.agenticsdlc.dto;

import java.time.Instant;
import java.util.List;

public record TestGenerationDraftResponse(
        String draftId,
        String sourceRelativePath,
        String testRelativePath,
        List<String> untestedMethods,
        String sourceCode,
        List<String> validationErrors,
        boolean valid,
        Instant createdAt
) {
}