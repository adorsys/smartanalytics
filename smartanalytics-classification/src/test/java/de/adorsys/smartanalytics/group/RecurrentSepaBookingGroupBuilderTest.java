package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecurrentSepaBookingGroupBuilderTest {

    private RecurrentSepaGroupBuilder sut;

    @Before
    public void setup() {
        sut = new RecurrentSepaGroupBuilder("", null);
    }

    @Test
    public void createCategory_should_create_no_categories_if_there_is_no_input() {
        BookingGroup result = sut.createGroup(null);

        assertThat(result).isNull();
    }

    @Test
    public void createCategory_should_create_no_categories_if_there_is_no_purpose() {
        Booking booking = new Booking();

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).isNull();
    }

    @Test
    public void createCategory_should_create_no_categories_if_there_is_no_mandat_in_purpose() {
        Booking booking = new Booking();
        booking.setPurpose("some purpose");

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).isNull();
    }

    @Test
    public void createCategory_should_create_no_categories_if_there_is_no_creditor_id() {
        Booking booking = new Booking();
        booking.setPurpose("mandat some value");

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).isNull();
    }

    @Test
    public void createCategory_should_create_one_category_if_there_is_a_mandat_and_a_creditor_id() {
        Booking booking = new Booking();
        booking.setMandateReference("mandat some value");
        booking.setCreditorId("some creditor id");
        booking.setPurpose("purpose");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        wrappedBooking.putUsedRule("rule1");

        BookingGroup result = sut.createGroup(wrappedBooking);

        assertThat(result).isNotNull();
    }
}
