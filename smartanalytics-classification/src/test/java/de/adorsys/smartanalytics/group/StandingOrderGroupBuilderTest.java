package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class StandingOrderGroupBuilderTest {

    private StandingOrderGroupBuilder sut;

    @Before
    public void setup() {
        sut = new StandingOrderGroupBuilder("");
    }

    @Test
    public void categoryShouldBeCreated_should_return_false_for_no_input() {

        boolean result = sut.groupShouldBeCreated(null);

        assertThat(result).as("category should be created").isFalse();
    }

    @Test
    public void categoryShouldBeCreated_should_return_true_for_standing_order() {
        Booking booking = new Booking();
        booking.setStandingOrder(true);

        boolean result = sut.groupShouldBeCreated(new WrappedBooking(booking));

        assertThat(result).as("category should be created").isTrue();
    }

    @Test
    public void categoryShouldBeCreated_should_return_false_for_null_type() {
        Booking booking = new Booking();

        boolean result = sut.groupShouldBeCreated(new WrappedBooking(booking));

        assertThat(result).as("category should be created").isFalse();
    }

    @Test
    public void categoryShouldBeCreated_should_return_false_for_income() {
        Booking booking = new Booking();
        booking.setStandingOrder(false);

        boolean result = sut.groupShouldBeCreated(new WrappedBooking(booking));

        assertThat(result).as("category should be created").isFalse();

    }

    @Test
    public void categoryShouldBeCreated_should_return_false_for_debit() {
        Booking booking = new Booking();
        booking.setStandingOrder(false);

        boolean result = sut.groupShouldBeCreated(new WrappedBooking(booking));

        assertThat(result).as("category should be created").isFalse();

    }

    @Test
    public void createCategory_should_return_one_category_when_no_rule_was_hit_and_purpose_is_empty() {
        Booking booking = new Booking();
        booking.setStandingOrder(true);
        booking.setAmount(new BigDecimal(5));

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).as("category for booking").isNotNull();
    }

    @Test
    public void createCategory_should_return_one_category_when_no_rule_was_hit_and_creditor_id_is_empty() {
        Booking booking = new Booking();
        booking.setStandingOrder(true);
        booking.setPurpose("Mandat something");
        booking.setAmount(new BigDecimal(5));

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).as("category for booking").isNotNull();
    }

    @Test
    public void createCategory_should_return_one_category_when_creditor_id_and_mandat_is_not_empty() {
        Booking booking = new Booking();
        booking.setStandingOrder(true);
        booking.setPurpose("mandat something ");
        booking.setMandateReference("mandat something ");
        booking.setCreditorId("some id");

        BookingGroup result = sut.createGroup(new WrappedBooking(booking));

        assertThat(result).as("category for booking").isNotNull();
    }

    @Test
    public void createCategory_should_return_one_category_when_rule_matched() {
        Booking booking = new Booking();
        booking.setStandingOrder(true);
        booking.setAmount(new BigDecimal(5));

        WrappedBooking wrappedBooking = new WrappedBooking();
        wrappedBooking.setBooking(booking);

        BookingGroup result = sut.createGroup(wrappedBooking);

        assertThat(result).as("category for booking").isNotNull();
    }
}
