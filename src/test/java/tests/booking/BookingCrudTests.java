package tests.booking;

import base.BaseTest;
import dataproviders.BookingDataProvider;
import io.qameta.allure.*;
import models.booking.BookingRequest;
import models.booking.BookingResponse;
import models.booking.PartialBookingRequest;
import org.testng.annotations.Test;
import services.BookingService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("CRUD Operations")
public class BookingCrudTests extends BaseTest {
    
    private final BookingService bookingService = new BookingService();
    private int createdBookingId;
    
    @Test(priority = 1)
    @Story("Create Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test creating a new booking with valid data")
    public void testCreateBooking() {
        // Arrange
        BookingRequest bookingRequest = BookingDataProvider.createValidBookingRequest();
        
        // Act
        BookingResponse response = bookingService.createBooking(bookingRequest);
        
        // Assert
        assertThat("Booking ID should not be null", response.getBookingid(), notNullValue());
        assertThat("Booking ID should be positive", response.getBookingid(), greaterThan(0));
        assertThat("First name should match", response.getBooking().getFirstname(), 
                equalTo(bookingRequest.getFirstname()));
        assertThat("Last name should match", response.getBooking().getLastname(), 
                equalTo(bookingRequest.getLastname()));
        assertThat("Total price should match", response.getBooking().getTotalprice(), 
                equalTo(bookingRequest.getTotalprice()));
        
        createdBookingId = response.getBookingid();
    }
    
    @Test(priority = 2, dependsOnMethods = "testCreateBooking")
    @Story("Read Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test retrieving an existing booking")
    public void testGetBooking() {
        // Act
        BookingRequest retrievedBooking = bookingService.getBooking(createdBookingId);
        
        // Assert
        assertThat("Retrieved booking should not be null", retrievedBooking, notNullValue());
        assertThat("First name should not be empty", retrievedBooking.getFirstname(), 
                not(emptyString()));
        assertThat("Last name should not be empty", retrievedBooking.getLastname(), 
                not(emptyString()));
        assertThat("Total price should be positive", retrievedBooking.getTotalprice(), 
                greaterThan(0));
    }
    
    @Test(priority = 3, dependsOnMethods = "testGetBooking")
    @Story("Update Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test updating an existing booking with complete data")
    public void testUpdateBooking() {
        // Arrange
        BookingRequest updateRequest = BookingDataProvider.createValidBookingRequest();
        
        // Act
        BookingRequest updatedBooking = bookingService.updateBooking(createdBookingId, updateRequest);
        
        // Assert
        assertThat("Updated first name should match", updatedBooking.getFirstname(), 
                equalTo(updateRequest.getFirstname()));
        assertThat("Updated last name should match", updatedBooking.getLastname(), 
                equalTo(updateRequest.getLastname()));
        assertThat("Updated total price should match", updatedBooking.getTotalprice(), 
                equalTo(updateRequest.getTotalprice()));
        assertThat("Updated deposit paid should match", updatedBooking.isDepositpaid(), 
                equalTo(updateRequest.isDepositpaid()));
    }
    
    @Test(priority = 4, dependsOnMethods = "testUpdateBooking")
    @Story("Partial Update Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test partially updating an existing booking")
    public void testPartialUpdateBooking() {
        // Arrange
        PartialBookingRequest partialRequest = BookingDataProvider.createPartialBookingRequest();
        
        // Act
        BookingRequest updatedBooking = bookingService.partialUpdateBooking(createdBookingId, partialRequest);
        
        // Assert
        assertThat("Partially updated first name should match", updatedBooking.getFirstname(), 
                equalTo(partialRequest.getFirstname()));
        assertThat("Partially updated last name should match", updatedBooking.getLastname(), 
                equalTo(partialRequest.getLastname()));
        // Other fields should remain unchanged from previous update
        assertThat("Total price should remain positive", updatedBooking.getTotalprice(), 
                greaterThan(0));
    }
    
    @Test(priority = 5, dependsOnMethods = "testPartialUpdateBooking")
    @Story("Delete Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test deleting an existing booking")
    public void testDeleteBooking() {
        // Act & Assert
        bookingService.deleteBooking(createdBookingId);
        
        // Verify deletion
        bookingService.verifyBookingDeleted(createdBookingId);
    }
}