package com.example.chaitanya.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.PromptMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/ollama")
@CrossOrigin("*")
public class OllamaController {

    private ChatClient chatClient;
    public OllamaController(OllamaChatModel chatModel){
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/{web}/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String web,@PathVariable String message){
        log.info("Received incoming prompt request execution payload");
        String response = chatClient
                .prompt(message)
                .call()
                .content();
        String model = chatClient
                .prompt(message)
                .call()
                .chatResponse().getMetadata().getModel();
        String usage = String.valueOf(chatClient
                .prompt(message)
                .call()
                .chatResponse().getMetadata().getUsage());
        String rateLimit = String.valueOf(chatClient
                .prompt(message)
                .call()
                .chatResponse().getMetadata().getRateLimit());
        log.info("Model used for the prompt: "+message+" is " + model+" , "+usage+" , "+web);
        return ResponseEntity.ok(response);
    }


}
