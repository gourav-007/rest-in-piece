package utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public class ResponseValidator {
    
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        assertThat("Status code validation failed", 
                response.getStatusCode(), equalTo(expectedStatusCode));
        log.info("Status code validation passed: {}", expectedStatusCode);
    }
    
    public static void validateResponseTime(Response response, long maxResponseTime) {
        assertThat("Response time validation failed", 
                response.getTime(), lessThan(maxResponseTime));
        log.info("Response time validation passed: {} ms", response.getTime());
    }
    
    public static void validateJsonSchema(Response response, String schemaPath) {
        InputStream schemaStream = ResponseValidator.class.getClassLoader()
                .getResourceAsStream(schemaPath);
        
        if (schemaStream == null) {
            throw new RuntimeException("Schema file not found: " + schemaPath);
        }
        
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
        log.info("JSON schema validation passed for: {}", schemaPath);
    }
    
    public static void validateJsonPath(Response response, String jsonPath, Matcher<?> matcher) {
        response.then().assertThat().body(jsonPath, matcher);
        log.info("JSON path validation passed for: {}", jsonPath);
    }
    
    public static void validateHeader(Response response, String headerName, String expectedValue) {
        assertThat("Header validation failed for: " + headerName,
                response.getHeader(headerName), equalTo(expectedValue));
        log.info("Header validation passed for: {} = {}", headerName, expectedValue);
    }
    
    public static void validateContentType(Response response, String expectedContentType) {
        assertThat("Content-Type validation failed",
                response.getContentType(), containsString(expectedContentType));
        log.info("Content-Type validation passed: {}", expectedContentType);
    }
    
    public static void validateNotNull(Response response, String jsonPath) {
        response.then().assertThat().body(jsonPath, notNullValue());
        log.info("Not null validation passed for: {}", jsonPath);
    }
    
    public static void validateArraySize(Response response, String jsonPath, int expectedSize) {
        response.then().assertThat().body(jsonPath, hasSize(expectedSize));
        log.info("Array size validation passed for: {} with size: {}", jsonPath, expectedSize);
    }
}