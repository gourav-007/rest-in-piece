package tests.booking;

import base.BaseTest;
import dataproviders.BookingDataProvider;
import io.qameta.allure.*;
import models.booking.BookingRequest;
import org.testng.annotations.Test;
import services.BookingService;

@Epic("Booking Management")
@Feature("Negative Test Scenarios")
public class BookingNegativeTests extends BaseTest {
    
    private final BookingService bookingService = new BookingService();
    
    @Test
    @Story("Invalid Booking Data")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test creating booking with invalid price")
    public void testCreateBookingWithInvalidPrice() {
        // Arrange
        BookingRequest invalidBooking = BookingDataProvider.createBookingWithInvalidPrice();
        
        // Act & Assert
        try {
            bookingService.createBooking(invalidBooking);
            // If we reach here, the test should fail as we expect an exception
            throw new AssertionError("Expected booking creation to fail with invalid price");
        } catch (Exception e) {
            // Expected behavior - booking creation should fail
            assert true;
        }
    }
    
    @Test
    @Story("Invalid Booking Data")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test creating booking with empty names")
    public void testCreateBookingWithEmptyNames() {
        // Arrange
        BookingRequest invalidBooking = BookingDataProvider.createBookingWithEmptyName();
        
        // Act & Assert
        try {
            bookingService.createBooking(invalidBooking);
            // If we reach here, the test should fail as we expect an exception
            throw new AssertionError("Expected booking creation to fail with empty names");
        } catch (Exception e) {
            // Expected behavior - booking creation should fail
            assert true;
        }
    }
    
    @Test
    @Story("Non-existent Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test retrieving a non-existent booking")
    public void testGetNonExistentBooking() {
        // Act & Assert
        try {
            bookingService.getBooking(999999); // Non-existent booking ID
            throw new AssertionError("Expected booking retrieval to fail for non-existent ID");
        } catch (Exception e) {
            // Expected behavior - should fail with 404
            assert true;
        }
    }
}