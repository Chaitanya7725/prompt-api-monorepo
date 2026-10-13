package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordTestRunRequest(
        @NotBlank String draftId,
        @NotBlank String status,
        String summary
) {
}