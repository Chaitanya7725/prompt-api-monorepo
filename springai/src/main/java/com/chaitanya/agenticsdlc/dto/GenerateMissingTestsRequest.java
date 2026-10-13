package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record GenerateMissingTestsRequest(
        @NotBlank String sourceRelativePath
) {
}
