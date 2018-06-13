package de.adorsys.smartanalytics.calculator;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingPeriodCalculatorTest {

    @Test
    public void booking_period_calculator_should_return_day_of_date2(){
        final int DAY_OF_MONTH_2 = 2;
        LocalDate date1 = LocalDate.of(2017, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 1, DAY_OF_MONTH_2);
        BookingPeriodCalculator bc1 = new BookingPeriodCalculator(date1, date2);

        assertThat(bc1.nextDay()).isEqualTo(DAY_OF_MONTH_2);
    }

    @Test
    public void booking_period_calculator_should_return_day_of_date1(){
        final int DAY_OF_MONTH_1 = 1;
        LocalDate date1 = LocalDate.of(2017, 1, DAY_OF_MONTH_1);
        LocalDate date2 = LocalDate.of(2017, 3, 2);
        BookingPeriodCalculator bc1 = new BookingPeriodCalculator(date1, date2);

        assertThat(bc1.nextDay()).isEqualTo(DAY_OF_MONTH_1);
    }

    @Test
    public void booking_period_calculator_should_return_fruehester_tag_equals_null(){
        LocalDate date1 = LocalDate.of(2016, 1, 1);
        LocalDate date2 = LocalDate.of(2017, 3, 2);
        BookingPeriodCalculator bc1 = new BookingPeriodCalculator(date1, date2);

       assertThat(bc1.nextDay()).isNull();
    }
}
