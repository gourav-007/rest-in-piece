package base;

import auth.AuthenticationManager;
import config.ConfigManager;
import core.BaseApiClient;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.TestDataUtils;

import java.lang.reflect.Method;

@Slf4j
public class BaseTest {
    
    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        log.info("Setting up test class for environment: {}", ConfigManager.getEnvironment());
        BaseApiClient.getRequestSpec(); // Initialize API client
    }
    
    @BeforeMethod(alwaysRun = true)
    public void setupMethod(Method method) {
        log.info("Starting test method: {}", method.getName());
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test failed: {}", result.getMethod().getMethodName());
            attachFailureInfo(result);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test passed: {}", result.getMethod().getMethodName());
        }
        
        // Clear authentication token after each test to ensure fresh state
        AuthenticationManager.clearToken();
    }
    
    @Attachment(value = "Failure Information", type = "text/plain")
    private String attachFailureInfo(ITestResult result) {
        StringBuilder info = new StringBuilder();
        info.append("Test Method: ").append(result.getMethod().getMethodName()).append("\n");
        info.append("Environment: ").append(ConfigManager.getEnvironment()).append("\n");
        info.append("Base URL: ").append(ConfigManager.getConfig().baseUrl()).append("\n");
        
        if (result.getThrowable() != null) {
            info.append("Error: ").append(result.getThrowable().getMessage()).append("\n");
            info.append("Stack Trace: ").append(result.getThrowable().toString());
        }
        
        return info.toString();
    }
}