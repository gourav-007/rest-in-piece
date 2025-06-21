package dataproviders;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import models.booking.BookingDates;
import models.booking.BookingRequest;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SecureTestDataManager {
    
    private static final Faker faker = new Faker();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final AtomicInteger bookingCounter = new AtomicInteger(1);
    private static final ConcurrentHashMap<String, Object> testDataCache = new ConcurrentHashMap<>();
    
    /**
     * Creates unique booking request for parallel test execution
     */
    public static BookingRequest createUniqueBookingRequest() {
        String threadId = Thread.currentThread().getName();
        int uniqueId = bookingCounter.getAndIncrement();
        
        BookingRequest booking = BookingRequest.builder()
                .firstname(faker.name().firstName() + "_" + uniqueId)
                .lastname(faker.name().lastName() + "_" + threadId)
                .totalprice(ThreadLocalRandom.current().nextInt(100, 2000))
                .depositpaid(ThreadLocalRandom.current().nextBoolean())
                .bookingdates(createValidBookingDates())
                .additionalneeds(faker.lorem().word())
                .build();
        
        // Cache for cleanup
        String key = threadId + "_" + uniqueId;
        testDataCache.put(key, booking);
        
        log.debug("Created unique booking for thread: {} with ID: {}", threadId, uniqueId);
        return booking;
    }
    
    /**
     * Creates booking with international characters
     */
    public static BookingRequest createInternationalBookingRequest() {
        return BookingRequest.builder()
                .firstname("José_" + bookingCounter.getAndIncrement())
                .lastname("Müller_" + Thread.currentThread().getName())
                .totalprice(faker.number().numberBetween(100, 2000))
                .depositpaid(true)
                .bookingdates(createValidBookingDates())
                .additionalneeds("Café ☕")
                .build();
    }
    
    /**
     * Creates booking with edge case data
     */
    public static BookingRequest createEdgeCaseBookingRequest() {
        return BookingRequest.builder()
                .firstname("A") // Minimum length
                .lastname("B".repeat(50)) // Long name
                .totalprice(1) // Minimum price
                .depositpaid(false)
                .bookingdates(createValidBookingDates())
                .additionalneeds("") // Empty additional needs
                .build();
    }
    
    private static BookingDates createValidBookingDates() {
        return BookingDates.builder()
                .checkin(dateFormat.format(faker.date().past(10, TimeUnit.DAYS)))
                .checkout(dateFormat.format(faker.date().future(5, TimeUnit.DAYS)))
                .build();
    }
    
    /**
     * Cleanup test data for current thread
     */
    public static void cleanupThreadData() {
        String threadId = Thread.currentThread().getName();
        testDataCache.entrySet().removeIf(entry -> entry.getKey().contains(threadId));
        log.debug("Cleaned up test data for thread: {}", threadId);
    }
    
    /**
     * Get cached test data count
     */
    public static int getCachedDataCount() {
        return testDataCache.size();
    }
}