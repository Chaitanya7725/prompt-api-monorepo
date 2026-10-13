package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.dto.JavaFileGenerationRequest;
import com.chaitanya.agenticsdlc.dto.JavaFileGenerationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JavaTestGenerationService {

    private final ChatClient chatClient;

    public JavaTestGenerationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public JavaFileGenerationResponse generateTest(
            JavaFileGenerationRequest request
    ) {
        String prompt = """
                You are a senior Java 21 and Spring Boot test engineer.

                Generate exactly one JUnit 5 unit-test Java source file.

                Project conventions:
                - Java 21.
                - JUnit Jupiter.
                - Use org.junit.jupiter.api.Test.
                - Use org.junit.jupiter.api.Assertions.
                - Do not use SpringBootTest.
                - Do not use Mockito unless a dependency is necessary.
                - The response must contain compilable Java source code.
                - Do not include Markdown code fences.
                - Do not include explanations before or after source code.
                - Do not create, alter, or delete any project file.
                - Test only public behavior.

                Requirement:
                %s

                File purpose:
                %s

                Target package:
                %s

                Target class name:
                %s
                """.formatted(
                request.requirement(),
                request.filePurpose(),
                request.packageName(),
                request.className()
        );

        String sourceCode = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return new JavaFileGenerationResponse(
                request.packageName(),
                request.className() + ".java",
                removeMarkdownFences(sourceCode),
                List.of(
                        "AI-generated draft: review before saving.",
                        "Run ./mvnw test after manually saving the file.",
                        "No file was written automatically."
                )
        );
    }

    private String removeMarkdownFences(String sourceCode) {
        return sourceCode
                .replace("```java", "")
                .replace("```", "")
                .trim();
    }

    public JavaFileGenerationResponse generateTestsFromSource(
            String productionSource,
            String testPackageName,
            String testClassName
    ) {
        String prompt = """
            You are a senior Java 21 and JUnit 5 test engineer.

            Generate exactly one compilable JUnit 5 unit-test Java source file
            for the production Java source supplied below.

            Strict rules:
            - Use JUnit Jupiter only.
            - Use org.junit.jupiter.api.Test.
            - Use org.junit.jupiter.api.Assertions.
            - Do not use SpringBootTest.
            - Do not use Mockito, repositories, databases, or HTTP calls
              unless they are explicitly present in the production source.
            - Call only public constructors and public methods that actually
              appear in the supplied production source.
            - Do not invent classes, methods, constructors, fields, or behavior.
            - Cover meaningful branches and edge cases visible in the source.
            - Produce only Java code with no Markdown fences or explanation.
            - The package must be: %s
            - The public test class must be: %s

            Production Java source:
            --- BEGIN SOURCE ---
            %s
            --- END SOURCE ---
            """.formatted(
                testPackageName,
                testClassName,
                productionSource
        );

        String sourceCode = chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .replace("```java", "")
                .replace("```", "")
                .trim();

        return new JavaFileGenerationResponse(
                testPackageName,
                testClassName + ".java",
                sourceCode,
                List.of(
                        "AI-generated test draft. Review before saving.",
                        "The service read source code only; it did not write a file.",
                        "Run ./mvnw test after saving the reviewed test."
                )
        );
    }

    public String generateMissingTests(
            String productionSource,
            String existingTestSource,
            String dependencyContext,
            String testPackageName,
            String testClassName,
            List<String> untestedMethods
    ) {
        String prompt = """
                You are a senior Java 21 and JUnit 5 test engineer.

                Generate one complete, compilable JUnit 5 test class.

                Strict rules:
                - Generate tests ONLY for the listed untested public methods.
                - Do not duplicate or remove existing tests.
                - Call only constructors, methods, and types visible in the
                  supplied production source.
                - Do not invent repositories, dependencies, methods, or fields.
                - Do not use SpringBootTest, Mockito, databases, HTTP calls,
                  filesystem calls, threads, or external services.
                - Use org.junit.jupiter.api.Test and assertions from
                  org.junit.jupiter.api.Assertions.
                - Return only Java source code.
                - Do not include Markdown fences or explanatory text.
                - Package: %s
                - Test class: %s
                
                Untested public methods:
                %s

                Production source:
                --- BEGIN PRODUCTION SOURCE ---
                %s
                --- END PRODUCTION SOURCE ---

                Project dependency public APIs:
                --- BEGIN DEPENDENCY CONTEXT ---
                %s
                --- END DEPENDENCY CONTEXT ---
                
                Existing test source, possibly empty:
                --- BEGIN EXISTING TEST SOURCE ---
                %s
                --- END EXISTING TEST SOURCE ---
                """.formatted(
                testPackageName,
                testClassName,
                untestedMethods,
                productionSource,
                dependencyContext,
                existingTestSource.isBlank()
                        ? "No existing test file."
                        : existingTestSource
        );

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .replace("```java", "")
                .replace("```", "")
                .trim();
    }
}