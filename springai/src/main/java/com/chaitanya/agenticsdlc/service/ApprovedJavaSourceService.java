package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApprovedJavaSourceService {

    private static final Path MAIN_SOURCE_ROOT = Path.of(
            "src/main/java/com/chaitanya/agenticsdlc"
    ).toAbsolutePath().normalize();

    private static final Path TEST_SOURCE_ROOT = Path.of(
            "src/test/java/com/chaitanya/agenticsdlc"
    ).toAbsolutePath().normalize();

    public String readProductionSource(String relativePath) {
        Path path = resolveApprovedJavaFile(MAIN_SOURCE_ROOT, relativePath);
        return readFile(path);
    }

    public String readMatchingTestSourceIfPresent(String productionRelativePath) {
        String testRelativePath = toTestRelativePath(productionRelativePath);

        Path testPath = resolveApprovedJavaFile(
                TEST_SOURCE_ROOT,
                testRelativePath
        );

        if (!Files.exists(testPath)) {
            return "";
        }

        return readFile(testPath);
    }

    public String toTestRelativePath(String productionRelativePath) {
        if (!productionRelativePath.endsWith(".java")) {
            throw new IllegalArgumentException("Only .java source files are allowed.");
        }

        String fileName = Path.of(productionRelativePath)
                .getFileName()
                .toString();

        String testFileName = fileName.replace(".java", "Test.java");

        Path parent = Path.of(productionRelativePath).getParent();

        return parent == null
                ? testFileName
                : parent.resolve(testFileName).toString();
    }

    public Path resolveTestOutputPath(String testRelativePath) {
        return resolveApprovedJavaFile(TEST_SOURCE_ROOT, testRelativePath);
    }

    private Path resolveApprovedJavaFile(Path root, String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            throw new IllegalArgumentException("Source path is required.");
        }

        Path path = root.resolve(relativePath).normalize();

        if (!path.startsWith(root)) {
            throw new IllegalArgumentException("Path is outside the approved directory.");
        }

        if (!path.toString().endsWith(".java")) {
            throw new IllegalArgumentException("Only Java files are allowed.");
        }

        return path;
    }

    private String readFile(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File not found: " + path);
        }

        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read source file.", exception);
        }
    }
}