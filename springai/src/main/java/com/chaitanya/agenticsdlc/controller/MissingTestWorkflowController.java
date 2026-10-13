package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.dto.ApproveGeneratedTestRequest;
import com.chaitanya.agenticsdlc.dto.GenerateMissingTestsRequest;
import com.chaitanya.agenticsdlc.dto.RecordTestRunRequest;
import com.chaitanya.agenticsdlc.dto.SaveGeneratedTestResponse;
import com.chaitanya.agenticsdlc.dto.TestGenerationDraftResponse;
import com.chaitanya.agenticsdlc.model.GeneratedTestDraft;
import com.chaitanya.agenticsdlc.service.ApprovedTestSaveService;
import com.chaitanya.agenticsdlc.service.MissingTestWorkflowService;
import com.chaitanya.agenticsdlc.service.TestRunAuditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/ai/tests")
public class MissingTestWorkflowController {

    private final MissingTestWorkflowService workflowService;
    private final ApprovedTestSaveService saveService;
    private final TestRunAuditService testRunAuditService;

    public MissingTestWorkflowController(
            MissingTestWorkflowService workflowService,
            ApprovedTestSaveService saveService,
            TestRunAuditService testRunAuditService
    ) {
        this.workflowService = workflowService;
        this.saveService = saveService;
        this.testRunAuditService = testRunAuditService;
    }

    @PostMapping(
            value = "/draft",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> generateDraft(
            @Valid @RequestBody GenerateMissingTestsRequest request
    ) {
        GeneratedTestDraft draft = workflowService.generateDraft(
                request.sourceRelativePath()
        );

        if (!draft.valid()) {
            String validationMessage = """
                DRAFT FAILED VALIDATION

                Draft ID: %s
                Validation errors:
                %s
                """.formatted(
                    draft.id(),
                    String.join("\n", draft.validationErrors())
            );

            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(validationMessage);
        }

        String response = """
            // AI GENERATED TEST DRAFT
            // Draft ID: %s
            // Source file: %s
            // Untested methods: %s
            // Review this source before calling /approve-save.

            %s
            """.formatted(
                draft.id(),
                draft.sourceRelativePath(),
                String.join(", ", draft.untestedMethods()),
                draft.sourceCode()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(response);
    }

    @PostMapping("/approve-save")
    @ResponseStatus(HttpStatus.CREATED)
    public SaveGeneratedTestResponse approveAndSave(
            @Valid @RequestBody ApproveGeneratedTestRequest request
    ) {
        Path savedPath = saveService.approveAndSave(request.draftId());

        return new SaveGeneratedTestResponse(
                savedPath.toString(),
                "Approved test draft saved. Run ./mvnw test manually."
        );
    }

    @PostMapping("/test-run")
    public TestRunAuditService.TestRunAudit recordTestRun(
            @Valid @RequestBody RecordTestRunRequest request
    ) {
        return testRunAuditService.record(
                request.draftId(),
                request.status(),
                request.summary()
        );
    }
}