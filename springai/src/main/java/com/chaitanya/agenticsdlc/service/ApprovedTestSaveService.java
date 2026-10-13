package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.model.GeneratedTestDraft;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class ApprovedTestSaveService {

    private final GeneratedTestDraftStore draftStore;
    private final ApprovedJavaSourceService sourceService;

    public ApprovedTestSaveService(
            GeneratedTestDraftStore draftStore,
            ApprovedJavaSourceService sourceService
    ) {
        this.draftStore = draftStore;
        this.sourceService = sourceService;
    }

    public Path approveAndSave(String draftId) {
        GeneratedTestDraft draft = draftStore.findById(draftId);

        if (!draft.valid()) {
            throw new IllegalStateException(
                    "Draft failed validation and cannot be saved: "
                            + draft.validationErrors()
            );
        }

        Path destination = sourceService.resolveTestOutputPath(
                draft.testRelativePath()
        );

        if (Files.exists(destination)) {
            throw new IllegalStateException(
                    "Test file already exists. Overwrite is not allowed: "
                            + destination
            );
        }

        try {
            Files.createDirectories(destination.getParent());

            Files.writeString(
                    destination,
                    draft.sourceCode(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW
            );

            return destination;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not save approved test file.",
                    exception
            );
        }
    }
}