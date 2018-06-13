package de.adorsys.smartanalytics.calendar;

import java.time.LocalDate;

public class SimpleHoliday implements Holiday {

    private int day;
    private int month;

    public SimpleHoliday(int day, int month) {
        this.day = day;
        this.month = month;
    }

    @Override
    public LocalDate getDayForYear(int year) {
        return LocalDate.of(year, month, day);
    }

}
