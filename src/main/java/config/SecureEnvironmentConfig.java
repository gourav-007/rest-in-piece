package config;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:environments/${environment}.properties",
    "classpath:environments/default.properties"
})
public interface SecureEnvironmentConfig extends Config {
    
    @Key("base.url")
    String baseUrl();
    
    // Use environment variables for sensitive data
    @Key("auth.username")
    @DefaultValue("${AUTH_USERNAME:admin}")
    String authUsername();
    
    @Key("auth.password")
    @DefaultValue("${AUTH_PASSWORD:password123}")
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
    
    @Key("ssl.verification.enabled")
    @DefaultValue("true")
    boolean sslVerificationEnabled();
    
    @Key("token.expiry.buffer.minutes")
    @DefaultValue("5")
    int tokenExpiryBufferMinutes();
}