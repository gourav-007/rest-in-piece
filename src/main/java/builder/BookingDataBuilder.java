package builder;

import com.github.javafaker.Faker;
import data.BookingData;
import data.BookingDates;
import data.PartialBookingData;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class BookingDataBuilder {

    private static final Faker faker = Faker.instance();

    public static BookingData bookingDataBuilder(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return BookingData
                .builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(1,2000))
                .depositpaid(true)
                .bookingdates(BookingDates.builder().checkin(dateFormat.format(faker.date().past(20, TimeUnit.DAYS))).checkout(dateFormat.format(faker.date().future(5,TimeUnit.DAYS)))
                        .build())
                .additionalneeds("Breakfast")
                .build();
    }

    public PartialBookingData partialBookingData(){
       return PartialBookingData
                .builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .build();

    }
}
