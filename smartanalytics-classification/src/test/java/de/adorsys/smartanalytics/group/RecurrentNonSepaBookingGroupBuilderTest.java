package de.adorsys.smartanalytics.group;


import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecurrentNonSepaBookingGroupBuilderTest {

    private RecurrentNonSepaGroupBuilder sut;

    @Before
    public void setup() {
        sut = new RecurrentNonSepaGroupBuilder("", null);
    }

    @Test
    public void createCategory_should_create_two_categories_if_a_rules_has_matched_and_amount_is_null() {
        Booking booking = new Booking();

        WrappedBooking wrappedBooking = new WrappedBooking();
        wrappedBooking.setBooking(booking);
        wrappedBooking.putUsedRule("test rule id");

        BookingGroup result = sut.createGroup(wrappedBooking);

        assertNotNull(result);
    }

    @Test
    public void createCategory_should_create_three_categories_if_a_rules_has_matched() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("0.00"));

        WrappedBooking wrappedBooking = new WrappedBooking();
        wrappedBooking.setBooking(booking);

        BookingGroup result = sut.createGroup(wrappedBooking);

        assertNotNull(result);
    }

    @Test
    public void createCategory_should_create_two_categories_if_a_rules_has_not_matched() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("0.00"));

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertNotNull(result);
    }

    @Test
    public void createCategory_should_create_no_categories_if_there_is_no_input() {

        BookingGroup result = sut.createGroup(null);

        assertNull(result);
    }

}
