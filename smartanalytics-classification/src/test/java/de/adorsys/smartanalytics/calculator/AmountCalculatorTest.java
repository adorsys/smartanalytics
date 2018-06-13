package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountCalculatorTest {

    private AmountCalculator sut;

    @Before
    public void setup() { sut = new AmountCalculator(); }

    @Test
    public void bestimme_betrag_should_return_null(){
        List<WrappedBooking> umsaetze = new ArrayList<>();

        BigDecimal result = sut.calcAmount(umsaetze);
        assertThat(result).isNull();
    }

    @Test
    public void bestimme_betrag_should_return_last_amount_if_quantity_less_or_equal_1(){
        Booking b1 = new Booking();
        b1.setAmount(new BigDecimal(100.00));

        List<WrappedBooking> umsaetze = Arrays.asList(new WrappedBooking(b1));

        BigDecimal result = sut.calcAmount(umsaetze);
        assertThat(result).isEqualTo("100");
    }

    @Test
    public void bestimme_betrag_should_return_average_of_two_bookings(){
        Booking b1 = new Booking();
        b1.setAmount(new BigDecimal(100.00));

        Booking b2 = new Booking();
        b2.setAmount(new BigDecimal(200.00));

        List<WrappedBooking> umsaetze = Arrays.asList(new WrappedBooking(b1), new WrappedBooking(b2));

        BigDecimal result = sut.calcAmount(umsaetze);
        assertThat(result).isEqualTo("150.00");
    }

    @Test
    public void bestimme_betrag_should_return_equal_amount_of_two_bookings(){
        Booking b1 = new Booking();
        b1.setAmount(new BigDecimal(100.00));

        Booking b2 = new Booking();
        b2.setAmount(new BigDecimal(100.00));

        Booking b3 = new Booking();
        b3.setAmount(new BigDecimal(200.00));

        List<WrappedBooking> umsaetze = Arrays.asList(new WrappedBooking(b1), new WrappedBooking(b2), new WrappedBooking(b3));

        BigDecimal result = sut.calcAmount(umsaetze);
        assertThat(result).isEqualTo("100.00");
    }

    @Test
    public void bestimme_betrag_should_return_average_of_three_bookings() {
        Booking b1 = new Booking();
        b1.setAmount(new BigDecimal(100.00));

        Booking b2 = new Booking();
        b2.setAmount(new BigDecimal(200.00));

        Booking b3 = new Booking();
        b3.setAmount(new BigDecimal(300.00));

        List<WrappedBooking> umsaetze = Arrays.asList(new WrappedBooking(b1), new WrappedBooking(b2), new WrappedBooking(b3));

        BigDecimal result = sut.calcAmount(umsaetze);
        assertThat(result).isEqualTo(new BigDecimal(200));
    }

}
