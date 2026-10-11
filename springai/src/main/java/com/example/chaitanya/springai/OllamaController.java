package com.example.chaitanya.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.PromptMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

import org.springframework.http.MediaType;
@Slf4j
@RestController
@RequestMapping("/api/ollama")
@CrossOrigin("*")
public class OllamaController {

    private final ChatClient chatClient;
//    public OllamaController(OllamaChatModel chatModel){
//        this.chatClient = ChatClient.create(chatModel);
//    }
//
//    // stateless QnA
//    @GetMapping("/{web}/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String web,@PathVariable String message){
//        log.info("Received incoming prompt request execution payload");
//        String response = chatClient
//                .prompt(message)
//                .call()
//                .content();
//        String model = chatClient
//                .prompt(message)
//                .call()
//                .chatResponse().getMetadata().getModel();
//        String usage = String.valueOf(chatClient
//                .prompt(message)
//                .call()
//                .chatResponse().getMetadata().getUsage());
//        String rateLimit = String.valueOf(chatClient
//                .prompt(message)
//                .call()
//                .chatResponse().getMetadata().getRateLimit());
//        log.info("Model used for the prompt: "+message+" is " + model+" , "+usage+" , "+web);
//        return ResponseEntity.ok(response);
//    }

//    private final ChatClient chatClient;

    // Constructor Injection configuring a stateful ChatClient session
    public OllamaController(ChatClient.Builder builder) {

        // 1. Correctly instantiate the sliding message window via its Builder pattern
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20) // Keeps the last 20 messages to manage your 8GB RAM footprint
                .build();

        // 2. Correctly construct the memory interceptor advisor via its Builder pattern
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();

        this.chatClient = builder
                .defaultSystem("You are a helpful, conversational AI chatbot.")
                .defaultAdvisors(memoryAdvisor) // Inject the active advisor pipeline layer
                .build();
    }

    @GetMapping(value = "/{web}/{message}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getAnswer(@PathVariable String message) {
        System.out.println("Processing user chat turn: " + message);

        String response = this.chatClient.prompt()
                .user(message)
                // Optional step: Ensures a stable context allocation key maps the history string
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "default-session"))
                .call()
                .content();

        return ResponseEntity.ok(response);
    }


}
