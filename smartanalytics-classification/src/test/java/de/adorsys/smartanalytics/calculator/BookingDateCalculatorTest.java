package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.Cycle;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.group.DummyBookingGroup;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingDateCalculatorTest {

    private BookingDateCalculator sut;

    @Before
    public void setup() { sut = new BookingDateCalculator(); }

    @Test
    public void ermittle_ausfuehrungs_termin_standard_should_return_empty_list(){
        DummyBookingGroup c1 = new DummyBookingGroup("12345");
        LocalDate startFinanzPeriode = LocalDate.of(2017, 1, 1);
        LocalDate endeFinanzPeriode = LocalDate.of(2017, 10, 1);
        LocalDate referenzDatum = LocalDate.of(2017, 5, 1);

        List<LocalDate> result = sut.calcBookingDates(c1, new ArrayList<>(), startFinanzPeriode, endeFinanzPeriode, referenzDatum);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void ermittle_ausfuehrungs_termin_standard_with_booking_should_return_empty_list(){
        DummyBookingGroup c1 = new DummyBookingGroup("12345");
        LocalDate startFinanzPeriode = LocalDate.of(2017, 1, 1);
        LocalDate endeFinanzPeriode = LocalDate.of(2017, 10, 1);
        LocalDate referenzDatum = LocalDate.of(2017, 5, 1);

        Booking b1 = new Booking();
        b1.setExecutionDate(startFinanzPeriode);

        List<LocalDate> result = sut.calcBookingDates(c1, Arrays.asList(new WrappedBooking(b1)), startFinanzPeriode, endeFinanzPeriode, referenzDatum);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void ermittle_ausfuehrungs_termin_standard_should_return_list_greater_zero(){
        DummyBookingGroup c1 = new DummyBookingGroup("12345");
        c1.setCycle(Cycle.YEARLY);

        LocalDate startFinanzPeriode = LocalDate.of(2017, 1, 1);
        LocalDate endeFinanzPeriode = LocalDate.of(2017, 10, 1);
        LocalDate referenzDatum = LocalDate.of(2017, 5, 1);
        LocalDate executionDate = LocalDate.of(2016, 5, 1);

        Booking b1 = new Booking();
        b1.setExecutionDate(executionDate);

        List<LocalDate> result = sut.calcBookingDates(c1, Arrays.asList(new WrappedBooking(b1)), startFinanzPeriode, endeFinanzPeriode, referenzDatum);
        assertThat(result.size()).isEqualTo(1);
    }


}

