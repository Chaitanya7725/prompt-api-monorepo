package com.chaitanya.agenticsdlc.policy;

import com.chaitanya.agenticsdlc.entity.Requirement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementPolicyService {

    private static final List<String> HIGH_RISK_TERMS = List.of(
            "production delete",
            "password",
            "secret",
            "credit card",
            "personal data",
            "customer data",
            "bypass security"
    );

    public PolicyDecision evaluate(Requirement requirement) {
        String text = (
                requirement.getTitle() + " " + requirement.getDescription()
        ).toLowerCase();

        boolean containsHighRiskTerm = HIGH_RISK_TERMS.stream()
                .anyMatch(text::contains);

        if (containsHighRiskTerm) {
            return new PolicyDecision(
                    true,
                    "High-risk terms detected. Human approval is mandatory."
            );
        }

        return new PolicyDecision(
                true,
                "All AI-generated recommendations require human approval."
        );
    }
}