package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.model.GeneratedTestDraft;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeneratedTestDraftStore {

    private final Map<String, GeneratedTestDraft> drafts =
            new ConcurrentHashMap<>();

    public GeneratedTestDraft save(
            String sourceRelativePath,
            String testRelativePath,
            java.util.List<String> untestedMethods,
            String sourceCode,
            java.util.List<String> validationErrors,
            boolean valid
    ) {
        String id = UUID.randomUUID().toString();

        GeneratedTestDraft draft = new GeneratedTestDraft(
                id,
                sourceRelativePath,
                testRelativePath,
                untestedMethods,
                sourceCode,
                validationErrors,
                valid,
                java.time.Instant.now()
        );

        drafts.put(id, draft);
        return draft;
    }

    public GeneratedTestDraft findById(String id) {
        GeneratedTestDraft draft = drafts.get(id);

        if (draft == null) {
            throw new IllegalArgumentException("Draft not found: " + id);
        }

        return draft;
    }
}