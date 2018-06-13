package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AttributeTest {

    @Test
    public void RFN_should_return_reference_name_attribute() {
        Booking booking = new Booking(); 
        booking.setReferenceName("test reference");

        String result = Attribute.RFN.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("TEST REFERENCE");
    }

    @Test
    public void GID_should_return_creditor_id_attribute() {
        Booking booking = new Booking();
        booking.setCreditorId("test creditor id");

        String result = Attribute.GID.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("TEST CREDITOR ID");
    }

    @Test
    public void VWZ_should_return_purpose_attribute() {
        Booking booking = new Booking();
        booking.setPurpose("test purpose");

        String result = Attribute.VWZ.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("TEST PURPOSE");
    }

    @Test
    public void IBAN_should_return_iban_attribute() {
        Booking booking = new Booking();
        booking.setIban("test iban");

        String result = Attribute.IBAN.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("TEST IBAN");
    }

    @Test
    public void REFERENZ_KONTO_should_return_account_number_attribute() {
        Booking booking = new Booking();
        booking.setAccountNumber("test account number");

        String result = Attribute.KTO.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("test account number");
    }

    @Test
    public void REFERENZ_BLZ_should_return_bank_code_attribute() {
        Booking booking = new Booking();
        booking.setBankCode("test bank code");

        String result = Attribute.BLZ.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("test bank code");
    }

    @Test
    public void AMOUNT_should_return_amount_attribute() {
        Booking booking = new Booking();
        booking.setAmount(new BigDecimal("123.23"));

        String result = Attribute.AMOUNT.value(new WrappedBooking(booking));

        assertThat(result).isEqualTo("123.23");
    }

    @Test
    public void HKAT_should_return_main_category_attribute() {
        WrappedBooking booking = new WrappedBooking();
        booking.setMainCategory("test main category");

        String result = Attribute.HKAT.value(booking);

        assertThat(result).isEqualTo("TEST MAIN CATEGORY");
    }

    @Test
    public void UKAT_should_return_sub_category_attribute() {
        WrappedBooking booking = new WrappedBooking();
        booking.setSubCategory("test sub category");

        String result = Attribute.UKAT.value(booking);

        assertThat(result).isEqualTo("TEST SUB CATEGORY");
    }

    @Test
    public void SPEZ_should_return_specification_attribute() {
        WrappedBooking booking = new WrappedBooking();
        booking.setSpecification("test specification");

        String result = Attribute.SPEZ.value(booking);

        assertThat(result).isEqualTo("TEST SPECIFICATION");
    }
}
