package com.marketplace.database;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class JsonDatabase {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public <T> void save(String filename, T data) {
        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.err.println("❌ Save error: " + e.getMessage());
        }
    }

    public <T> T load(String filename, Class<T> clazz) {
        try {
            File file = new File(filename);
            if (!file.exists()) return null;
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            System.err.println("❌ Load error: " + e.getMessage());
            return null;
        }
    }
}