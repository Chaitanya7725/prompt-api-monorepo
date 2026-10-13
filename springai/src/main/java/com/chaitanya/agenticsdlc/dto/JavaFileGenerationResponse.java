package com.chaitanya.agenticsdlc.dto;

import java.util.List;

public record JavaFileGenerationResponse(
        String packageName,
        String fileName,
        String sourceCode,
        List<String> warnings
) {
}
