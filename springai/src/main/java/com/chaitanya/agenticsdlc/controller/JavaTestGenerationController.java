package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.dto.JavaFileGenerationRequest;
import com.chaitanya.agenticsdlc.dto.JavaFileGenerationResponse;
import com.chaitanya.agenticsdlc.service.JavaTestGenerationService;
import com.chaitanya.agenticsdlc.dto.GenerateTestsFromSourceRequest;
import com.chaitanya.agenticsdlc.service.JavaSourceReaderService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class JavaTestGenerationController {

    private final JavaTestGenerationService javaTestGenerationService;
    private final JavaSourceReaderService javaSourceReaderService;

    public JavaTestGenerationController(
            JavaTestGenerationService javaTestGenerationService,
            JavaSourceReaderService javaSourceReaderService
    ) {
        this.javaTestGenerationService = javaTestGenerationService;
        this.javaSourceReaderService = javaSourceReaderService;
    }

    @PostMapping("/generate-test")
    public JavaFileGenerationResponse generateTest(
            @Valid @RequestBody JavaFileGenerationRequest request
    ) {
        return javaTestGenerationService.generateTest(request);
    }

//    @PostMapping("/decode-source")
//    public String decodeSource(@RequestBody JavaFileGenerationResponse copiedValue) {
//        return jsonStringParserService.parseJsonString(copiedValue.sourceCode());
//    }

    @PostMapping(
            value = "/generate-test-source",
            produces = "text/plain"
    )
    public ResponseEntity<String> generateTestSource(
            @Valid @RequestBody JavaFileGenerationRequest request
    ) {
        JavaFileGenerationResponse generated =
                javaTestGenerationService.generateTest(request);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(generated.sourceCode());
    }


    @PostMapping("/generate-tests-from-source")
    public ResponseEntity<String> generateTestsFromSource(
            @Valid @RequestBody GenerateTestsFromSourceRequest request
    ) {
        String productionSource = javaSourceReaderService.readApprovedJavaFile(
                request.sourceRelativePath()
        );

        JavaFileGenerationResponse generated =
                javaTestGenerationService.generateTestsFromSource(
                        productionSource,
                        request.testPackageName(),
                        request.testClassName()
                );

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(generated.sourceCode());
    }
}