package core;

import config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;

@Slf4j
public class BaseApiClient {
    
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    
    static {
        setupRequestSpecification();
        setupResponseSpecification();
    }
    
    private static void setupRequestSpecification() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getConfig().baseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();
        
        RestAssured.requestSpecification = requestSpec;
        log.info("Request specification configured for base URL: {}", 
                ConfigManager.getConfig().baseUrl());
    }
    
    private static void setupResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan((long) ConfigManager.getConfig().readTimeout()))
                .build();
        
        RestAssured.responseSpecification = responseSpec;
        log.info("Response specification configured with timeout: {} ms", 
                ConfigManager.getConfig().readTimeout());
    }
    
    public static RequestSpecification getRequestSpec() {
        return requestSpec;
    }
    
    public static ResponseSpecification getResponseSpec() {
        return responseSpec;
    }
}