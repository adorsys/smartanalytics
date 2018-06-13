package de.adorsys.smartanalytics.group;


import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import java.math.BigDecimal;

import static de.adorsys.smartanalytics.group.RecurrentSepaGroupBuilder.isSepaBooking;
import static org.assertj.core.api.Assertions.assertThat;

public class SepaBookingValidatorTest {

    @Test
    public void isSepaBooking_should_return_false_if_amount_is_positive() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));

        boolean result = isSepaBooking(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is SEPA booking").isFalse();
    }

    @Test
    public void isSepaBooking_should_return_false_if_amount_is_zero() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("0.00"));

        boolean result = isSepaBooking(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is SEPA booking").isFalse();
    }

    @Test
    public void isSepaBooking_should_return_false_if_purpose_is_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));

        boolean result = isSepaBooking(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is SEPA booking").isFalse();
    }

    @Test
    public void isSepaBooking_should_return_false_if_creditor_id_is_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));
        booking.setPurpose("test purpose");

        boolean result = isSepaBooking(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is SEPA booking").isFalse();
    }

    @Test
    public void isSepaBooking_should_return_true_if_creditor_id_is_exists() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));
        booking.setPurpose("test purpose");
        booking.setCreditorId("invalid creditor id");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        wrappedBooking.putUsedRule("rule1");

        boolean result = isSepaBooking(wrappedBooking, null);

        assertThat(result).as("Booking is SEPA booking").isTrue();
    }
}
