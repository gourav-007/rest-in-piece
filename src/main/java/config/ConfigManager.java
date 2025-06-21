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
}