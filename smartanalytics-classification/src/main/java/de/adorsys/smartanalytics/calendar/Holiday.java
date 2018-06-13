package de.adorsys.smartanalytics.calendar;

import java.time.LocalDate;

public interface Holiday {

    LocalDate getDayForYear(int year);

}
