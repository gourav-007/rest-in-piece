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
    
    /**
     * Execute operation with retry policy for operations that return a value
     * @param operation Supplier that returns a value
     * @return Result of the operation
     */
    public static <T> T executeWithRetry(Supplier<T> operation) {
        return Failsafe.with(retryPolicy).get(() -> operation.get());
    }
    
    /**
     * Execute operation with retry policy for void operations
     * @param operation Runnable operation to execute
     */
    public static void executeWithRetry(Runnable operation) {
        Failsafe.with(retryPolicy).run(() -> operation.run());
    }
    
    /**
     * Execute operation with custom retry policy
     * @param customPolicy Custom retry policy
     * @param operation Supplier operation
     * @return Result of the operation
     */
    public static <T> T executeWithCustomRetry(RetryPolicy<T> customPolicy, Supplier<T> operation) {
        return Failsafe.with(customPolicy).get(() -> operation.get());
    }
    
    /**
     * Create a custom retry policy for specific scenarios
     * @param maxAttempts Maximum retry attempts
     * @param delaySeconds Delay between retries in seconds
     * @return Custom retry policy
     */
    public static <T> RetryPolicy<T> createCustomRetryPolicy(int maxAttempts, int delaySeconds) {
        return RetryPolicy.<T>builder()
                .handle(Exception.class)
                .withDelay(Duration.ofSeconds(delaySeconds))
                .withMaxRetries(maxAttempts)
                .onRetry(event -> log.warn("Custom retry attempt {} for operation", event.getAttemptCount()))
                .onFailure(event -> log.error("Custom retry failed after {} attempts", event.getAttemptCount()))
                .build();
    }
}