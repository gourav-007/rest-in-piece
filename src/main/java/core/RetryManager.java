package core;

import config.ConfigManager;
import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
public class RetryManager {
    
    private static final RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
            .handle(Exception.class)
            .withDelay(Duration.ofSeconds(ConfigManager.getConfig().retryDelaySeconds()))
            .withMaxRetries(ConfigManager.getConfig().maxRetryAttempts())
            .onRetry(event -> log.warn("Retry attempt {} for operation", event.getAttemptCount()))
            .onFailure(event -> log.error("Operation failed after {} attempts", event.getAttemptCount()))
            .build();
    
    public static <T> T executeWithRetry(Supplier<T> operation) {
        return Failsafe.with(retryPolicy).get(operation);
    }
    
    public static void executeWithRetry(Runnable operation) {
        Failsafe.with(retryPolicy).run(operation);
    }
}