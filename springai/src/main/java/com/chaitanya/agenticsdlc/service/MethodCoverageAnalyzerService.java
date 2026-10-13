package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MethodCoverageAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(MethodCoverageAnalyzerService.class);
    private static final Pattern PUBLIC_METHOD_PATTERN = Pattern.compile(
            "(?m)^\\s*public\\s+(?:static\\s+)?"
                    + "[\\w<>\\[\\], ?]+\\s+"
                    + "(\\w+)\\s*\\([^;{}]*\\)\\s*"
                    + "(?:throws\\s+[\\w., ]+)?\\s*\\{"
    );

    public Set<String> findUntestedPublicMethods(
            String productionSource,
            String existingTestSource
    ) {
        Set<String> publicMethods = extractPublicMethodNames(productionSource);

        publicMethods.removeAll(Set.of(
                "equals",
                "hashCode",
                "toString",
                "getClass"
        ));

        log.info("Detected public methods: {}", publicMethods);

        Set<String> methodsCalledInTests = new LinkedHashSet<>();

        for (String methodName : publicMethods) {
            if (existingTestSource.contains("." + methodName + "(")) {
                methodsCalledInTests.add(methodName);
            }
        }

        log.info("Methods detected in existing tests: {}", methodsCalledInTests);

        publicMethods.removeAll(methodsCalledInTests);

        log.info("Untested public methods: {}", publicMethods);

        return publicMethods;
    }

    private Set<String> extractPublicMethodNames(String source) {
        Set<String> methodNames = new LinkedHashSet<>();
        Matcher matcher = PUBLIC_METHOD_PATTERN.matcher(source);

        while (matcher.find()) {
            String methodName = matcher.group(1);

            if (!isControlKeyword(methodName)) {
                methodNames.add(methodName);
            }
        }

        return methodNames;
    }

    private boolean isControlKeyword(String value) {
        return Set.of(
                "if",
                "for",
                "while",
                "switch",
                "catch"
        ).contains(value);
    }
}