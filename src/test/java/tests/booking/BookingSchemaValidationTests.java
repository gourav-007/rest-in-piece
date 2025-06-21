package tests.booking;

import base.BaseTest;
import dataproviders.BookingDataProvider;
import io.qameta.allure.*;
import models.booking.BookingRequest;
import org.testng.annotations.Test;
import utils.ResponseValidator;
import core.BaseApiClient;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

@Epic("Booking Management")
@Feature("Schema Validation")
public class BookingSchemaValidationTests extends BaseTest {
    
    @Test
    @Story("Response Schema Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that booking creation response matches expected JSON schema")
    public void testCreateBookingSchemaValidation() {
        // Arrange
        BookingRequest bookingRequest = BookingDataProvider.createValidBookingRequest();
        
        // Act
        Response response = given()
                .spec(BaseApiClient.getRequestSpec())
                .body(bookingRequest)
                .when()
                .post("/booking")
                .then()
                .extract()
                .response();
        
        // Assert
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateJsonSchema(response, "schemas/create-booking-response-schema.json");
    }
    
    @Test
    @Story("Response Schema Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that booking retrieval response matches expected JSON schema")
    public void testGetBookingSchemaValidation() {
        // First create a booking
        BookingRequest bookingRequest = BookingDataProvider.createValidBookingRequest();
        
        Response createResponse = given()
                .spec(BaseApiClient.getRequestSpec())
                .body(bookingRequest)
                .when()
                .post("/booking")
                .then()
                .extract()
                .response();
        
        int bookingId = createResponse.jsonPath().getInt("bookingid");
        
        // Act - Get the booking
        Response getResponse = given()
                .spec(BaseApiClient.getRequestSpec())
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract()
                .response();
        
        // Assert
        ResponseValidator.validateStatusCode(getResponse, 200);
        ResponseValidator.validateJsonSchema(getResponse, "schemas/get-booking-response-schema.json");
    }
}