package com.chaitanya.agenticsdlc.policy;

public record PolicyDecision(
        boolean requiresHumanApproval,
        String reason
) {
}
