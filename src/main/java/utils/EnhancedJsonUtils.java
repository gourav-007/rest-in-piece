package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class EnhancedJsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    public static String toJson(Object object) {
        if (object == null) {
            log.warn("Attempting to convert null object to JSON");
            return "null";
        }
        
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", object.getClass().getSimpleName(), e);
            throw new RuntimeException("Failed to convert object to JSON: " + e.getMessage(), e);
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            log.warn("Attempting to convert null/empty JSON to object: {}", clazz.getSimpleName());
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }
        
        if (clazz == null) {
            throw new IllegalArgumentException("Target class cannot be null");
        }
        
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object: {}. JSON: {}", clazz.getSimpleName(), 
                    json.length() > 200 ? json.substring(0, 200) + "..." : json, e);
            throw new RuntimeException("Failed to convert JSON to " + clazz.getSimpleName() + ": " + e.getMessage(), e);
        }
    }
    
    public static JsonNode parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }
        
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON: {}", 
                    json.length() > 200 ? json.substring(0, 200) + "..." : json, e);
            throw new RuntimeException("Failed to parse JSON: " + e.getMessage(), e);
        }
    }
    
    public static JsonNode loadJsonFromResource(String resourcePath) {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource path cannot be null or empty");
        }
        
        try (InputStream inputStream = EnhancedJsonUtils.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            log.error("Error loading JSON from resource: {}", resourcePath, e);
            throw new RuntimeException("Failed to load JSON from resource: " + resourcePath, e);
        }
    }
    
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    /**
     * Safely extract string value from JSON path
     */
    public static String extractStringValue(JsonNode node, String path, String defaultValue) {
        try {
            JsonNode valueNode = node.at("/" + path.replace(".", "/"));
            return valueNode.isMissingNode() ? defaultValue : valueNode.asText();
        } catch (Exception e) {
            log.warn("Failed to extract value from path: {}", path, e);
            return defaultValue;
        }
    }
}