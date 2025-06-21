package auth;

import config.ConfigManager;
import core.BaseApiClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import utils.SecurityUtils;

import static io.restassured.RestAssured.given;

@Slf4j
public class SecureAuthenticationManager {
    
    private static String cachedToken;
    private static long tokenExpiryTime;
    private static final Object TOKEN_LOCK = new Object();
    
    public static String getAuthToken() {
        synchronized (TOKEN_LOCK) {
            if (isTokenValid()) {
                log.debug("Using cached authentication token");
                return cachedToken;
            }
            return refreshToken();
        }
    }
    
    private static boolean isTokenValid() {
        if (cachedToken == null) {
            return false;
        }
        
        // Add buffer time before token expires
        long bufferTime = ConfigManager.getConfig().tokenExpiryBufferMinutes() * 60 * 1000;
        return System.currentTimeMillis() < (tokenExpiryTime - bufferTime);
    }
    
    private static String refreshToken() {
        log.info("Refreshing authentication token");
        
        try {
            AuthRequest authRequest = AuthRequest.builder()
                    .username(ConfigManager.getConfig().authUsername())
                    .password(ConfigManager.getConfig().authPassword())
                    .build();
            
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .body(authRequest)
                    .when()
                    .post("/auth")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
            
            cachedToken = response.jsonPath().getString("token");
            
            if (cachedToken == null || cachedToken.trim().isEmpty()) {
                throw new RuntimeException("Received empty token from authentication service");
            }
            
            // Set token expiry to 1 hour from now
            tokenExpiryTime = System.currentTimeMillis() + (60 * 60 * 1000);
            
            log.info("Authentication token refreshed successfully");
            log.debug("Token expires at: {}", new java.util.Date(tokenExpiryTime));
            
            return cachedToken;
            
        } catch (Exception e) {
            log.error("Failed to refresh authentication token", e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
    
    public static void clearToken() {
        synchronized (TOKEN_LOCK) {
            if (cachedToken != null) {
                log.debug("Clearing authentication token: {}****", 
                    SecurityUtils.maskToken(cachedToken));
            }
            cachedToken = null;
            tokenExpiryTime = 0;
            log.info("Authentication token cleared");
        }
    }
    
    public static boolean isAuthenticated() {
        return isTokenValid();
    }
}