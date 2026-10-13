package com.chaitanya.agenticsdlc.dto;

import java.util.List;

public record AiAnalysisResponse(
        String summary,
        List<String> acceptanceCriteria,
        List<String> risks,
        String recommendation
) {
}
