package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratedTestValidatorService {

    private static final List<String> FORBIDDEN_TOKENS = List.of(
            "Runtime.getRuntime",
            "ProcessBuilder",
            "System.exit",
            "Files.delete",
            "Files.write",
            "FileOutputStream",
            "Socket",
            "HttpClient",
            "@SpringBootTest",
            "```"
    );

    public List<String> validate(
            String sourceCode,
            String expectedPackageName,
            String expectedClassName,
            List<String> expectedMethods
    ) {
        List<String> errors = new ArrayList<>();

        if (sourceCode == null || sourceCode.isBlank()) {
            errors.add("Generated source code is empty.");
            return errors;
        }

        if (!sourceCode.contains(
                "package " + expectedPackageName + ";"
        )) {
            errors.add("Generated file has an unexpected package declaration.");
        }

        if (!sourceCode.contains(
                "class " + expectedClassName
        )) {
            errors.add("Generated file does not contain the expected test class.");
        }

        if (!sourceCode.contains("@Test")) {
            errors.add("Generated file has no @Test method.");
        }

//        if (sourceCode.contains(".getMessage()")
//                || sourceCode.contains(".isApproved()")) {
//            errors.add(
//                    "Generated code uses an unverified JavaBean-style accessor."
//            );
//        }

        for (String forbiddenToken : FORBIDDEN_TOKENS) {
            if (sourceCode.contains(forbiddenToken)) {
                errors.add("Forbidden code detected: " + forbiddenToken);
            }
        }

        for (String method : expectedMethods) {
            if (!sourceCode.contains("." + method + "(")) {
                errors.add(
                        "Generated test does not call untested method: " + method
                );
            }
        }

        return errors;
    }
}
