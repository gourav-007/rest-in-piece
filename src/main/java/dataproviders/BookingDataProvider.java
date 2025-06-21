package dataproviders;

import com.github.javafaker.Faker;
import models.booking.BookingDates;
import models.booking.BookingRequest;
import models.booking.PartialBookingRequest;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class BookingDataProvider {
    
    private static final Faker faker = new Faker();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static BookingRequest createValidBookingRequest() {
        return BookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(100, 2000))
                .depositpaid(true)
                .bookingdates(createValidBookingDates())
                .additionalneeds("Breakfast")
                .build();
    }
    
    public static BookingRequest createBookingWithInvalidPrice() {
        return BookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(-100) // Invalid negative price
                .depositpaid(true)
                .bookingdates(createValidBookingDates())
                .additionalneeds("Breakfast")
                .build();
    }
    
    public static BookingRequest createBookingWithEmptyName() {
        return BookingRequest.builder()
                .firstname("") // Empty firstname
                .lastname("") // Empty lastname
                .totalprice(faker.number().numberBetween(100, 2000))
                .depositpaid(true)
                .bookingdates(createValidBookingDates())
                .additionalneeds("Breakfast")
                .build();
    }
    
    public static PartialBookingRequest createPartialBookingRequest() {
        return PartialBookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .build();
    }
    
    public static BookingDates createValidBookingDates() {
        return BookingDates.builder()
                .checkin(dateFormat.format(faker.date().past(20, TimeUnit.DAYS)))
                .checkout(dateFormat.format(faker.date().future(5, TimeUnit.DAYS)))
                .build();
    }
    
    public static BookingDates createInvalidBookingDates() {
        return BookingDates.builder()
                .checkin(dateFormat.format(faker.date().future(5, TimeUnit.DAYS)))
                .checkout(dateFormat.format(faker.date().past(20, TimeUnit.DAYS))) // Checkout before checkin
                .build();
    }
}