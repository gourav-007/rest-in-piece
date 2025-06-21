package models.booking;

import lombok.Data;

@Data
public class BookingResponse {
    private int bookingid;
    private BookingRequest booking;
}