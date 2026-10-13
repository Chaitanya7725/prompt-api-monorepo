package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.model.GeneratedTestDraft;
import com.chaitanya.agenticsdlc.tools.BuildTools;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
public class MissingTestWorkflowService {

    private final ApprovedJavaSourceService sourceService;
    private final MethodCoverageAnalyzerService coverageAnalyzer;
    private final JavaTestGenerationService testGenerationService;
    private final GeneratedTestValidatorService validatorService;
    private final GeneratedTestDraftStore draftStore;
    private final JavaDependencyContextService dependencyContextService;


    public MissingTestWorkflowService(
            ApprovedJavaSourceService sourceService,
            MethodCoverageAnalyzerService coverageAnalyzer,
            JavaTestGenerationService testGenerationService,
            GeneratedTestValidatorService validatorService,
            GeneratedTestDraftStore draftStore,
            JavaDependencyContextService dependencyContextService) {
        this.sourceService = sourceService;
        this.coverageAnalyzer = coverageAnalyzer;
        this.testGenerationService = testGenerationService;
        this.validatorService = validatorService;
        this.draftStore = draftStore;
        this.dependencyContextService = dependencyContextService;
    }

    public GeneratedTestDraft generateDraft(String sourceRelativePath) {
        String productionSource =
                sourceService.readProductionSource(sourceRelativePath);

        String existingTestSource =
                sourceService.readMatchingTestSourceIfPresent(sourceRelativePath);

        Set<String> untestedSet = coverageAnalyzer.findUntestedPublicMethods(
                productionSource,
                existingTestSource
        );

        List<String> untestedMethods = List.copyOf(untestedSet);

        if (untestedMethods.isEmpty()) {
            throw new IllegalStateException(
                    "No untested public methods were detected."
            );
        }

        String testRelativePath =
                sourceService.toTestRelativePath(sourceRelativePath);

        String packageName = packageNameFromRelativePath(testRelativePath);

        String testClassName = Path.of(testRelativePath)
                .getFileName()
                .toString()
                .replace(".java", "");

        String dependencyContext =
                dependencyContextService.extractProjectDependencyContext(
                        productionSource
                );

        String generatedSource = testGenerationService.generateMissingTests(
                productionSource,
                existingTestSource,
                dependencyContext,
                packageName,
                testClassName,
                untestedMethods
        );

        List<String> validationErrors = validatorService.validate(
                generatedSource,
                packageName,
                testClassName,
                untestedMethods
        );

        return draftStore.save(
                sourceRelativePath,
                testRelativePath,
                untestedMethods,
                generatedSource,
                validationErrors,
                validationErrors.isEmpty()
        );
    }

    private String packageNameFromRelativePath(String relativePath) {
        Path parent = Path.of(relativePath).getParent();

        if (parent == null) {
            return "com.chaitanya.agenticsdlc";
        }

        return "com.chaitanya.agenticsdlc."
                + parent.toString()
                .replace("/", ".")
                .replace("\\", ".");
    }

}