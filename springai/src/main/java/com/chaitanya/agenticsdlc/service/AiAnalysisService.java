package com.chaitanya.agenticsdlc.service;

import com.chaitanya.agenticsdlc.dto.AiAnalysisResponse;
import com.chaitanya.agenticsdlc.entity.Requirement;
import com.chaitanya.agenticsdlc.tools.BuildTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;
import com.chaitanya.agenticsdlc.prompt.PromptTemplates;

@Service
public class AiAnalysisService {

    private final ChatClient chatClient;
    private final BuildTools buildTools;

    public AiAnalysisService(ChatClient.Builder chatClientBuilder, BuildTools buildTools) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.buildTools = buildTools;
    }

    public AiAnalysisResponse analyze(Requirement requirement) {
        String prompt = PromptTemplates.REQUIREMENT_ANALYSIS_V1
                .replace("{title}", requirement.getTitle())
                .replace("{description}", requirement.getDescription());

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(
                        AiAnalysisResponse.class,
                        options -> options.validateSchema()
                );
    }

    public String askBuildAssistant(String userMessage) {
        return chatClient.prompt()
                .system("""
                    You are a local build assistant.
                    You may call run_maven_clean only when the user explicitly
                    asks to clean the Maven project.
                    Never claim a command ran unless the tool result confirms it.
                    """)
                .user(userMessage)
                .tools(buildTools)
                .call()
                .content();
    }
}