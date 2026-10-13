package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class JavaSourceReaderService {

    private static final Path ALLOWED_SOURCE_ROOT = Path.of(
            "src/main/java/com/chaitanya/agenticsdlc"
    ).toAbsolutePath().normalize();

    public String readApprovedJavaFile(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            throw new IllegalArgumentException("Source path is required.");
        }

        Path requestedPath = ALLOWED_SOURCE_ROOT
                .resolve(relativePath)
                .normalize();

        if (!requestedPath.startsWith(ALLOWED_SOURCE_ROOT)) {
            throw new IllegalArgumentException("Path outside allowed source directory.");
        }

        if (!requestedPath.toString().endsWith(".java")) {
            throw new IllegalArgumentException("Only Java source files are allowed.");
        }

        if (!Files.exists(requestedPath)) {
            throw new IllegalArgumentException(
                    "Source file was not found: " + relativePath
            );
        }

        try {
            return Files.readString(requestedPath);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not read Java source file.",
                    exception
            );
        }
    }
}