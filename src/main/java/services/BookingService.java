package services;

import auth.AuthenticationManager;
import core.BaseApiClient;
import core.RetryManager;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import models.booking.BookingRequest;
import models.booking.BookingResponse;
import models.booking.PartialBookingRequest;
import utils.JsonUtils;
import utils.ResponseValidator;

import static io.restassured.RestAssured.given;

@Slf4j
public class BookingService {
    
    private static final String BOOKING_ENDPOINT = "/booking";
    
    @Step("Create new booking")
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        log.info("Creating new booking for: {} {}", 
                bookingRequest.getFirstname(), bookingRequest.getLastname());
        
        return RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .body(bookingRequest)
                    .when()
                    .post(BOOKING_ENDPOINT)
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            ResponseValidator.validateNotNull(response, "bookingid");
            
            String responseBody = response.getBody().asString();
            log.info("Booking created successfully with response: {}", responseBody);
            
            return JsonUtils.fromJson(responseBody, BookingResponse.class);
        });
    }
    
    @Step("Get booking by ID: {bookingId}")
    public BookingRequest getBooking(int bookingId) {
        log.info("Retrieving booking with ID: {}", bookingId);
        
        return RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .when()
                    .get(BOOKING_ENDPOINT + "/" + bookingId)
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            String responseBody = response.getBody().asString();
            log.info("Booking retrieved successfully: {}", responseBody);
            
            return JsonUtils.fromJson(responseBody, BookingRequest.class);
        });
    }
    
    @Step("Update booking with ID: {bookingId}")
    public BookingRequest updateBooking(int bookingId, BookingRequest bookingRequest) {
        log.info("Updating booking with ID: {}", bookingId);
        
        return RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .header("Cookie", "token=" + AuthenticationManager.getAuthToken())
                    .body(bookingRequest)
                    .when()
                    .put(BOOKING_ENDPOINT + "/" + bookingId)
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            String responseBody = response.getBody().asString();
            log.info("Booking updated successfully: {}", responseBody);
            
            return JsonUtils.fromJson(responseBody, BookingRequest.class);
        });
    }
    
    @Step("Partially update booking with ID: {bookingId}")
    public BookingRequest partialUpdateBooking(int bookingId, PartialBookingRequest partialRequest) {
        log.info("Partially updating booking with ID: {}", bookingId);
        
        return RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .header("Cookie", "token=" + AuthenticationManager.getAuthToken())
                    .body(partialRequest)
                    .when()
                    .patch(BOOKING_ENDPOINT + "/" + bookingId)
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 200);
            ResponseValidator.validateContentType(response, "application/json");
            
            String responseBody = response.getBody().asString();
            log.info("Booking partially updated successfully: {}", responseBody);
            
            return JsonUtils.fromJson(responseBody, BookingRequest.class);
        });
    }
    
    @Step("Delete booking with ID: {bookingId}")
    public void deleteBooking(int bookingId) {
        log.info("Deleting booking with ID: {}", bookingId);
        
        RetryManager.executeWithRetry(() -> {
            Response response = given()
                    .spec(BaseApiClient.getRequestSpec())
                    .header("Cookie", "token=" + AuthenticationManager.getAuthToken())
                    .when()
                    .delete(BOOKING_ENDPOINT + "/" + bookingId)
                    .then()
                    .extract()
                    .response();
            
            ResponseValidator.validateStatusCode(response, 201);
            log.info("Booking deleted successfully");
        });
    }
    
    @Step("Verify booking is deleted: {bookingId}")
    public void verifyBookingDeleted(int bookingId) {
        log.info("Verifying booking with ID {} is deleted", bookingId);
        
        Response response = given()
                .spec(BaseApiClient.getRequestSpec())
                .when()
                .get(BOOKING_ENDPOINT + "/" + bookingId)
                .then()
                .extract()
                .response();
        
        ResponseValidator.validateStatusCode(response, 404);
        log.info("Booking deletion verified successfully");
    }
}