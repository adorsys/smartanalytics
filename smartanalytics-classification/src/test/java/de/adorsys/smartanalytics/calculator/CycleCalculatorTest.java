package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Cycle;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CycleCalculatorTest {

    @Test
    public void aus_daten_should_return_yearly_cycle(){
        LocalDate date = LocalDate.of(2017, 1, 1);
        List<LocalDate> daten = Arrays.asList(date);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.YEARLY);
    }

    /**
     * notice: at least 6 bookings are necessary for weekly cycle
     * 'MIND_ANZAHL_UMSAETZE_FUER_WOECHENTLICHES_INTERVALL=6' in CycleCalculator */
    @Test
    public void aus_daten_should_return_weekly_cycle(){
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date8 = LocalDate.of(2017, 1, 7);
        LocalDate date2 = LocalDate.of(2017, 1, 14);
        LocalDate date3 = LocalDate.of(2017, 1, 21);
        LocalDate date4 = LocalDate.of(2017, 1, 28);
        LocalDate date5 = LocalDate.of(2017, 2, 5);
        LocalDate date6 = LocalDate.of(2017, 2, 12);
        LocalDate date7 = LocalDate.of(2017, 2, 19);

        List<LocalDate> daten = Arrays.asList(date1, date2, date3, date4, date5, date6, date7, date8);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.WEEKLY);
    }

    @Test
    public void aus_daten_should_return_monthly_cycle(){
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 2, 1);
        LocalDate date3 = LocalDate.of(2017, 3, 1);
        LocalDate date4 = LocalDate.of(2017, 4, 1);

        List<LocalDate> daten = Arrays.asList(date1, date2, date3, date4);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.MONTHLY);
    }

    @Test
    public void aus_date_should_return_two_monthly_cycle(){
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 3, 1);
        LocalDate date3 = LocalDate.of(2017, 5, 1);
        LocalDate date4 = LocalDate.of(2017, 7, 1);

        List<LocalDate> daten = Arrays.asList(date1, date2, date3, date4);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.TWO_MONTHLY);
    }

    @Test
    public void aus_date_should_return_quarterly_cycle(){
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 3, 1);
        LocalDate date3 = LocalDate.of(2017, 6, 1);
        LocalDate date4 = LocalDate.of(2017, 9, 1);

        List<LocalDate> daten = Arrays.asList(date1, date2, date3, date4);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.QUARTERLY);
    }

    @Test
    public void aus_date_should_return_halfyearly_cycle(){
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 7, 1);

        List<LocalDate> daten = Arrays.asList(date1, date2);

        Cycle result = CycleCalculator.fromDates(daten);
        assertThat(result).isEqualTo(Cycle.HALF_YEARLY);
    }
}
