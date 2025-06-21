package tests.core;

import base.BaseTest;
import core.RetryManager;
import dev.failsafe.RetryPolicy;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Core Framework")
@Feature("Retry Mechanism")
public class RetryManagerTests extends BaseTest {
    
    @Test
    @Story("Successful Operation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that successful operations execute without retry")
    public void testSuccessfulOperationWithoutRetry() {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);
        
        // Act
        String result = RetryManager.executeWithRetry(() -> {
            counter.incrementAndGet();
            return "success";
        });
        
        // Assert
        assertThat("Operation should succeed", result, equalTo("success"));
        assertThat("Should execute only once", counter.get(), equalTo(1));
    }
    
    @Test
    @Story("Failed Operation with Retry")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that failed operations are retried according to policy")
    public void testFailedOperationWithRetry() {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);
        
        // Act & Assert
        try {
            RetryManager.executeWithRetry(() -> {
                counter.incrementAndGet();
                throw new RuntimeException("Simulated failure");
            });
        } catch (Exception e) {
            // Expected to fail after retries
            assertThat("Should retry multiple times", counter.get(), greaterThan(1));
            assertThat("Should not exceed max retries", counter.get(), lessThanOrEqualTo(4)); // 1 initial + 3 retries
        }
    }
    
    @Test
    @Story("Void Operation Retry")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test retry mechanism for void operations")
    public void testVoidOperationRetry() {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);
        
        // Act
        RetryManager.executeWithRetry(() -> {
            counter.incrementAndGet();
            // Successful void operation
        });
        
        // Assert
        assertThat("Void operation should execute once", counter.get(), equalTo(1));
    }
    
    @Test
    @Story("Custom Retry Policy")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test custom retry policy creation and usage")
    public void testCustomRetryPolicy() {
        // Arrange
        RetryPolicy<String> customPolicy = RetryManager.createCustomRetryPolicy(2, 1);
        AtomicInteger counter = new AtomicInteger(0);
        
        // Act
        String result = RetryManager.executeWithCustomRetry(customPolicy, () -> {
            counter.incrementAndGet();
            if (counter.get() < 2) {
                throw new RuntimeException("Fail first attempt");
            }
            return "success on retry";
        });
        
        // Assert
        assertThat("Should succeed on retry", result, equalTo("success on retry"));
        assertThat("Should execute twice", counter.get(), equalTo(2));
    }
}