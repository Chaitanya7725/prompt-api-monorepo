package com.chaitanya.agenticsdlc.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MavenBuildService {

    private static final Path PROJECT_ROOT = Path.of("")
            .toAbsolutePath()
            .normalize();

    public MavenCommandResult clean() {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "./mvnw",
                "clean"
        );

        processBuilder.directory(PROJECT_ROOT.toFile());
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            String output;

            try (BufferedReader reader = process.inputReader()) {
                output = reader.lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            }

            int exitCode = process.waitFor();

            return new MavenCommandResult(
                    exitCode == 0,
                    exitCode,
                    output
            );

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not start Maven Wrapper. Ensure ./mvnw exists and is executable.",
                    exception
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Maven clean was interrupted.",
                    exception
            );
        }
    }

    public record MavenCommandResult(
            boolean successful,
            int exitCode,
            String output
    ) {
    }
}