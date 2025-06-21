package tests.security;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import utils.SecurityUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Security Testing")
@Feature("Data Protection and Security Validation")
public class SecurityValidationTests extends BaseTest {
    
    @Test
    @Story("Token Masking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that authentication tokens are properly masked in logs")
    public void testTokenMasking() {
        // Arrange
        String sensitiveToken = "abc123def456ghi789";
        
        // Act
        String maskedToken = SecurityUtils.maskToken(sensitiveToken);
        
        // Assert
        assertThat("Token should be masked", maskedToken, containsString("****"));
        assertThat("Token should not contain full original value", maskedToken, not(containsString("def456ghi789")));
        assertThat("Masked token should show first 4 characters", maskedToken, startsWith("abc1"));
    }
    
    @Test
    @Story("Sensitive Data Detection")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify detection of sensitive data in strings")
    public void testSensitiveDataDetection() {
        // Test cases
        assertThat("Should detect password", 
                SecurityUtils.containsSensitiveData("{\"password\":\"secret\"}"), is(true));
        assertThat("Should detect token", 
                SecurityUtils.containsSensitiveData("{\"token\":\"abc123\"}"), is(true));
        assertThat("Should not detect normal data", 
                SecurityUtils.containsSensitiveData("{\"name\":\"John\"}"), is(false));
    }
    
    @Test
    @Story("JSON Data Masking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that sensitive data in JSON is properly masked")
    public void testJsonDataMasking() {
        // Arrange
        String sensitiveJson = "{\"username\":\"admin\",\"password\":\"secret123\",\"token\":\"abc123def\"}";
        
        // Act
        String maskedJson = SecurityUtils.maskSensitiveData(sensitiveJson);
        
        // Assert
        assertThat("Password should be masked", maskedJson, containsString("\"password\":\"****\""));
        assertThat("Token should be masked", maskedJson, containsString("\"token\":\"****\""));
        assertThat("Username should not be masked", maskedJson, containsString("\"username\":\"admin\""));
    }
}