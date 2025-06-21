package config;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:environments/${environment}.properties",
    "classpath:environments/default.properties"
})
public interface EnvironmentConfig extends Config {
    
    @Key("base.url")
    String baseUrl();
    
    @Key("auth.username")
    String authUsername();
    
    @Key("auth.password")
    String authPassword();
    
    @Key("timeout.connection")
    @DefaultValue("30000")
    int connectionTimeout();
    
    @Key("timeout.read")
    @DefaultValue("60000")
    int readTimeout();
    
    @Key("retry.max.attempts")
    @DefaultValue("3")
    int maxRetryAttempts();
    
    @Key("retry.delay.seconds")
    @DefaultValue("2")
    int retryDelaySeconds();
    
    @Key("parallel.thread.count")
    @DefaultValue("5")
    int parallelThreadCount();
    
    @Key("database.url")
    String databaseUrl();
    
    @Key("database.username")
    String databaseUsername();
    
    @Key("database.password")
    String databasePassword();
}