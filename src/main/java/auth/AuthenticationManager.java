package auth;

import config.ConfigManager;
import core.BaseApiClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
public class AuthenticationManager {
    
    private static String cachedToken;
    private static long tokenExpiryTime;
    
    public static String getAuthToken() {
        if (isTokenValid()) {
            log.info("Using cached authentication token");
            return cachedToken;
        }
        
        return refreshToken();
    }
    
    private static boolean isTokenValid() {
        return cachedToken != null && System.currentTimeMillis() < tokenExpiryTime;
    }
    
    private static String refreshToken() {
        log.info("Refreshing authentication token");
        
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
        // Set token expiry to 1 hour from now (adjust based on your API)
        tokenExpiryTime = System.currentTimeMillis() + (60 * 60 * 1000);
        
        log.info("Authentication token refreshed successfully");
        return cachedToken;
    }
    
    public static void clearToken() {
        cachedToken = null;
        tokenExpiryTime = 0;
        log.info("Authentication token cleared");
    }
}