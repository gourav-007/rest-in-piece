package utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtils {
    
    private static final String MASK_PATTERN = "****";
    
    /**
     * Masks sensitive token for logging
     */
    public static String maskToken(String token) {
        if (token == null || token.length() < 8) {
            return MASK_PATTERN;
        }
        return token.substring(0, 4) + MASK_PATTERN;
    }
    
    /**
     * Masks sensitive data in JSON strings
     */
    public static String maskSensitiveData(String json) {
        if (json == null) {
            return null;
        }
        
        return json
            .replaceAll("(\"password\"\\s*:\\s*\")[^\"]*\"", "$1" + MASK_PATTERN + "\"")
            .replaceAll("(\"token\"\\s*:\\s*\")[^\"]*\"", "$1" + MASK_PATTERN + "\"")
            .replaceAll("(\"authorization\"\\s*:\\s*\")[^\"]*\"", "$1" + MASK_PATTERN + "\"");
    }
    
    /**
     * Validates if string contains potentially sensitive data
     */
    public static boolean containsSensitiveData(String data) {
        if (data == null) {
            return false;
        }
        
        String lowerData = data.toLowerCase();
        return lowerData.contains("password") || 
               lowerData.contains("token") || 
               lowerData.contains("authorization") ||
               lowerData.contains("secret");
    }
}