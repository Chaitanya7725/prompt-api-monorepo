package com.chaitanya.agenticsdlc.tools;

import com.chaitanya.agenticsdlc.service.MavenBuildService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class BuildTools {

    private final MavenBuildService mavenBuildService;

    public BuildTools(MavenBuildService mavenBuildService) {
        this.mavenBuildService = mavenBuildService;
    }

    @Tool(
            name = "run_maven_clean",
            description = """
                    Runs exactly './mvnw clean' in the approved local project root.
                    Use this only after the user explicitly asks to clean the project.
                    This tool cannot run any other command.
                    """
    )
    public String runMavenClean() {
        MavenBuildService.MavenCommandResult result =
                mavenBuildService.clean();

        return """
                successful: %s
                exitCode: %d
                output:
                %s
                """.formatted(
                result.successful(),
                result.exitCode(),
                result.output()
        );
    }
}