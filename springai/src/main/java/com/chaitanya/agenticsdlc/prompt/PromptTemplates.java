package com.chaitanya.agenticsdlc.prompt;

public final class PromptTemplates {

    private PromptTemplates() {
    }

    public static final String REQUIREMENT_ANALYSIS_V1 = """
            You are an enterprise software requirements analyst.

            Analyze the software requirement below.

            Rules:
            - Be concise and factual.
            - Create specific and testable acceptance criteria.
            - Identify security, privacy, and operational risks.
            - Do not invent requirements not present in the input.
            - recommendation must be exactly one of:
              APPROVE_FOR_HUMAN_REVIEW
              NEEDS_CLARIFICATION

            Requirement title:
            {title}

            Requirement description:
            {description}
            """;

    public static final String JAVA_FILE_GENERATION_V1 = """
            You are a senior Java and Spring Boot engineer.

            Generate one production-ready Java source file based only on the
            requirement and architecture context below.

            Rules:
            - Follow Java 21 and Spring Boot conventions.
            - Produce only source code, without Markdown fences or explanation.
            - Do not include secrets, API keys, or passwords.
            - Do not create database schema changes unless explicitly requested.
            - If information is missing, return a short TODO comment rather than inventing behavior.

            Requirement:
            {requirement}

            Target file purpose:
            {filePurpose}

            Target package:
            {packageName}

            Target class name:
            {className}
            """;
}