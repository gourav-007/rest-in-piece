package test;

import base.BaseSetup;
import builder.BookingDataBuilder;
import builder.TokenBuilder;
import data.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestfulBookerE2ETests extends BaseSetup {

    private BookingData newBooking;
    private BookingData updatedBooking;
    private PartialBookingData partialUpdateBooking;
    private Tokencreds tokenCreds;
    private int bookingId;

    @BeforeClass
    public void testSetup() {
        BookingDataBuilder builder = new BookingDataBuilder();
        TokenBuilder tokenBuild = new TokenBuilder();
        newBooking = builder.bookingDataBuilder();
        updatedBooking = builder.bookingDataBuilder();
        partialUpdateBooking = builder.partialBookingData();
        tokenCreds = tokenBuild.tokenBuilder();
    }

    @Test
    public void createBookingTest(){
        bookingId = given()
                .body(newBooking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(newBooking.getFirstname()))
                .body("booking.lastname", equalTo(newBooking.getLastname()))
                .body("booking.totalprice", equalTo(newBooking.getTotalprice()))
                .body("booking.depositpaid", equalTo(newBooking.isDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(newBooking.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(newBooking.getBookingdates().getCheckout()))
                .body("booking.additionalneeds", equalTo(newBooking.getAdditionalneeds()))
                .extract()
                .path("bookingid");
    }

    @Test
    public void testCreateBookingJsonSchema(){
        InputStream createBookingJsonSchema = getClass().getClassLoader().getResourceAsStream("createbookingjsonschema.json");
        given()
                .body(newBooking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(createBookingJsonSchema))
                .and()
                .extract()
                .path("bookingid");
    }

    @Test
    public void updateBookingTest(){
        given()
                .body(updatedBooking)
                .when()
                .header("Cookie","token="+generateToken())
                .put("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname",equalTo(updatedBooking.getFirstname()))
                .body("lastname",equalTo(updatedBooking.getLastname()))
                .body("totalprice",equalTo(updatedBooking.getTotalprice()))
                .body("depositpaid",equalTo(updatedBooking.isDepositpaid()))
                .body("bookingdates.checkin",equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout",equalTo(updatedBooking.getBookingdates().getCheckout()))
                .body("additionalneeds",equalTo(updatedBooking.getAdditionalneeds()));
    }


    @Test
    public void updatePartialBookingTest(){
        given()
                .body(partialUpdateBooking)
                .when()
                .header("Cookie","token="+generateToken())
                .patch("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("firstname",equalTo(partialUpdateBooking.getFirstname()))
                .body("lastname",equalTo(partialUpdateBooking.getLastname()))
                .body("totalprice",equalTo(updatedBooking.getTotalprice()))
                .body("depositpaid",equalTo(updatedBooking.isDepositpaid()))
                .body("bookingdates.checkin",equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout",equalTo(updatedBooking.getBookingdates().getCheckout()))
                .body("additionalneeds",equalTo(updatedBooking.getAdditionalneeds()));
    }

    @Test
    public void deleteBookingTest(){
        given()
                .header("Cookie","token="+generateToken())
                .when()
                .delete("/booking/"+bookingId)
                .then()
                .statusCode(201);
    }

    @Test
    public void checkBookingIsDeleted(){
        given()
                .get("/booking/"+bookingId)
                .then()
                .statusCode(404);
    }

    private String generateToken(){
        return given()
                .body(tokenCreds)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("token",not(nullValue()))
                .extract()
                .path("token");
    }
}
