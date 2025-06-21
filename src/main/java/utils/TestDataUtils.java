package utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestDataUtils {
    
    private static final Map<String, JsonNode> testDataCache = new HashMap<>();
    
    public static JsonNode getTestData(String fileName) {
        if (testDataCache.containsKey(fileName)) {
            return testDataCache.get(fileName);
        }
        
        String resourcePath = "testdata/" + fileName;
        JsonNode testData = JsonUtils.loadJsonFromResource(resourcePath);
        testDataCache.put(fileName, testData);
        
        log.info("Loaded test data from: {}", resourcePath);
        return testData;
    }
    
    public static JsonNode getTestDataByKey(String fileName, String key) {
        JsonNode testData = getTestData(fileName);
        JsonNode result = testData.get(key);
        
        if (result == null) {
            throw new RuntimeException("Test data key not found: " + key + " in file: " + fileName);
        }
        
        return result;
    }
    
    public static String getTestDataAsString(String fileName, String key) {
        JsonNode node = getTestDataByKey(fileName, key);
        return node.isTextual() ? node.asText() : node.toString();
    }
    
    public static void clearCache() {
        testDataCache.clear();
        log.info("Test data cache cleared");
    }
}