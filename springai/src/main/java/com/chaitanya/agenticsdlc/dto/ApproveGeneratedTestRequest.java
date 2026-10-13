package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record ApproveGeneratedTestRequest(
        @NotBlank String draftId
) {
}