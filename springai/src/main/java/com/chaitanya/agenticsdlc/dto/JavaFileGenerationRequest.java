package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record JavaFileGenerationRequest(

        @NotBlank
        String requirement,

        @NotBlank
        String filePurpose,

        @NotBlank
        String packageName,

        @NotBlank
        String className
) {
}