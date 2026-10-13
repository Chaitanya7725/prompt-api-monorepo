package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record BuildAssistantRequest(
        @NotBlank String message
) {
}