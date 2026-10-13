package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;

public record GenerateTestsFromSourceRequest(

        @NotBlank
        String sourceRelativePath,

        @NotBlank
        String testPackageName,

        @NotBlank
        String testClassName
) {
}