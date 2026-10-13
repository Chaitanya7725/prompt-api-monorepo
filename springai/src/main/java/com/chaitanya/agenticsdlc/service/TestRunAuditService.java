package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestRunAuditService {

    private final Map<String, TestRunAudit> testRuns =
            new ConcurrentHashMap<>();

    public TestRunAudit record(
            String draftId,
            String status,
            String summary
    ) {
        TestRunAudit audit = new TestRunAudit(
                draftId,
                status,
                summary == null ? "" : summary,
                Instant.now()
        );

        testRuns.put(draftId, audit);
        return audit;
    }

    public record TestRunAudit(
            String draftId,
            String status,
            String summary,
            Instant recordedAt
    ) {
    }
}