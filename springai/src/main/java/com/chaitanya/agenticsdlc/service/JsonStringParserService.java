package com.chaitanya.agenticsdlc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonStringParserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String parseJsonString(String copiedValue) {
        try {
            return objectMapper.readValue(copiedValue, String.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(
                    "The value is not a valid JSON string.",
                    exception
            );
        }
    }
}