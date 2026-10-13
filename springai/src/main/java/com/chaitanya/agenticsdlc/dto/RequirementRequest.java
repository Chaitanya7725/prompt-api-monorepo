package com.chaitanya.agenticsdlc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequirementRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must be at most 150 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 20, max = 5000, message = "Description must be 20 to 5000 characters")
        String description
) {
}