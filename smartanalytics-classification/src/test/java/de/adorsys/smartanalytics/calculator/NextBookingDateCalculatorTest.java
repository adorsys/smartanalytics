package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class NextBookingDateCalculatorTest {

    @Test
    public void Should_Not_Throw_An_Exception() {
        Booking b1 = new Booking();
        b1.setExecutionDate(LocalDate.parse("2017-01-24"));

        Booking b2 = new Booking();
        b2.setExecutionDate(LocalDate.parse("2017-01-24"));

        Booking b3 = new Booking();
        b3.setExecutionDate(LocalDate.parse("2017-01-24"));

        List<WrappedBooking> bookings = Arrays.asList(new WrappedBooking(b1), new WrappedBooking(b2), new WrappedBooking(b3));

        NextBookingDateCalculator.calcNextBookingDate(bookings, false, null);
    }
}
