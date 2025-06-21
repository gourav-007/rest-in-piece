package config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigManager {
    
    private static EnvironmentConfig environmentConfig;
    
    static {
        String environment = System.getProperty("environment", "dev");
        System.setProperty("environment", environment);
        environmentConfig = ConfigFactory.create(EnvironmentConfig.class);
    }
    
    public static EnvironmentConfig getConfig() {
        return environmentConfig;
    }
    
    public static String getEnvironment() {
        return System.getProperty("environment", "dev");
    }
    
    /**
     * Reload configuration (useful for testing)
     */
    public static void reloadConfig() {
        String environment = System.getProperty("environment", "dev");
        System.setProperty("environment", environment);
        environmentConfig = ConfigFactory.create(EnvironmentConfig.class);
    }
    
    /**
     * Set environment programmatically
     */
    public static void setEnvironment(String environment) {
        System.setProperty("environment", environment);
        reloadConfig();
    }
}