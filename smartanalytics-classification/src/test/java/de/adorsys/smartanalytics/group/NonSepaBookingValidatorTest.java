package de.adorsys.smartanalytics.group;


import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import java.math.BigDecimal;

import static de.adorsys.smartanalytics.group.RecurrentNonSepaGroupBuilder.isRecurrent;
import static org.assertj.core.api.Assertions.assertThat;

public class NonSepaBookingValidatorTest {

    @Test
    public void isRecurrent_should_return_false_if_amount_is_positive() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));

        boolean result = isRecurrent(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is recurrent non SEPA booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_if_amount_is_zero() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("0.00"));

        boolean result = isRecurrent(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is recurrent non SEPA booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_if_purpose_is_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));

        boolean result = isRecurrent(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is recurrent non SEPA booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_if_creditor_id_is_valid() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));
        booking.setPurpose("test purpose");
        booking.setCreditorId("DE12ABC01234567890");

        boolean result = isRecurrent(new WrappedBooking(booking), null);

        assertThat(result).as("Booking is recurrent non SEPA booking").isFalse();
    }
}
