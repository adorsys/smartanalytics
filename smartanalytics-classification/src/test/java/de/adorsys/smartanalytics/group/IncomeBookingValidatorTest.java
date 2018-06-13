package de.adorsys.smartanalytics.group;


import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import java.math.BigDecimal;

import static de.adorsys.smartanalytics.group.RecurrentIncomeGroupBuilder.isRecurrent;
import static org.assertj.core.api.Assertions.assertThat;

public class IncomeBookingValidatorTest {

    @Test
    public void isRecurrent_should_return_false_if_amount_is_negative() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("-1000.00"));

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_if_purpose_is_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_if_it_has_no_bank_connection() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_true_when_iban_is_not_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");
        booking.setIban("Test iban");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isTrue();
    }

    @Test
    public void isRecurrent_should_return_false_when_bank_code_is_not_null_but_account_number_is_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");
        booking.setBankCode("Test bank code");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_when_bank_code_is_null_but_account_number_is_not_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");
        booking.setAccountNumber("Test account number");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isFalse();
    }

    @Test
    public void isRecurrent_should_return_false_when_bank_code_and_account_number_is_not_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");
        booking.setBankCode("Test bank code");
        booking.setAccountNumber("Test account number");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isTrue();
    }

    @Test
    public void isRecurrent_should_return_true_when_reference_name_is_not_null() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("1000.00"));
        booking.setPurpose("Test purpose");
        booking.setReferenceName("Test reference");

        boolean result = isRecurrent(new WrappedBooking(booking));

        assertThat(result).as("Booking is recurrent income booking").isTrue();
    }
}
