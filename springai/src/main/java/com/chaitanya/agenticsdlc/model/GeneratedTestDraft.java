package com.chaitanya.agenticsdlc.model;

import java.time.Instant;
import java.util.List;

public record GeneratedTestDraft(
        String id,
        String sourceRelativePath,
        String testRelativePath,
        List<String> untestedMethods,
        String sourceCode,
        List<String> validationErrors,
        boolean valid,
        Instant createdAt
) {
}