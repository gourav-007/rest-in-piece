package core;

import config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import utils.SecurityLoggingFilter;

@Slf4j
public class SecureBaseApiClient {
    
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    
    static {
        setupRestAssuredConfig();
        setupRequestSpecification();
        setupResponseSpecification();
    }
    
    private static void setupRestAssuredConfig() {
        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", ConfigManager.getConfig().connectionTimeout())
                        .setParam("http.socket.timeout", ConfigManager.getConfig().readTimeout())
                        .setParam("http.connection-manager.max-total", 100)
                        .setParam("http.connection-manager.max-per-route", 20))
                .sslConfig(SSLConfig.sslConfig()
                        .relaxedHTTPSValidation(!ConfigManager.getConfig().sslVerificationEnabled()));
        
        RestAssured.config = config;
        log.info("REST Assured configuration initialized with SSL verification: {}", 
                ConfigManager.getConfig().sslVerificationEnabled());
    }
    
    private static void setupRequestSpecification() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getConfig().baseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "API-Test-Framework/2.0")
                .addFilter(new SecurityLoggingFilter()) // Custom secure logging
                .addFilter(new AllureRestAssured())
                .build();
        
        RestAssured.requestSpecification = requestSpec;
        log.info("Secure request specification configured for base URL: {}", 
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