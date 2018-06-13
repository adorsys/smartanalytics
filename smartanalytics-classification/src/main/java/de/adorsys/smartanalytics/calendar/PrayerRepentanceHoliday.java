package de.adorsys.smartanalytics.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class PrayerRepentanceHoliday implements Holiday {

    @Override
    public LocalDate getDayForYear(int year) {
        LocalDate date = LocalDate.of(year, 11, 16);

        while (date.getDayOfWeek() != DayOfWeek.WEDNESDAY) {
            date = date.plusDays(1);
        }

        return date;
    }
}
