package de.adorsys.smartanalytics.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BankCalendar {

    public static final Holiday KARFREITAG = new EasterHoliday(-2);
    public static final Holiday OSTERMONTAG = new EasterHoliday(1);
    public static final Holiday CHRISTI_HIMMELFAHRT = new EasterHoliday(39);
    public static final Holiday PFINGSTMONTAG = new EasterHoliday(50);

    public static final Holiday NEUJAHR = new SimpleHoliday(1, 1);
    public static final Holiday TAG_DER_ARBEIT = new SimpleHoliday(1, 5);
    public static final Holiday TAG_DER_DEUTSCHEN_EINHEIT = new SimpleHoliday(3, 10);
    public static final Holiday HEILIG_ABEND = new SimpleHoliday(24, 12);
    public static final Holiday ERSTER_WEIHNACHTS_FEIERTAG = new SimpleHoliday(25, 12);
    public static final Holiday ZWEITER_WEIHNACHTS_FEIERTAG = new SimpleHoliday(26, 12);
    public static final Holiday SYLVESTER = new SimpleHoliday(31, 12);

    public static final Holiday[] FEIERTAGE = new Holiday[]{
            KARFREITAG,
            OSTERMONTAG,
            CHRISTI_HIMMELFAHRT,
            PFINGSTMONTAG,
            NEUJAHR,
            TAG_DER_ARBEIT,
            TAG_DER_DEUTSCHEN_EINHEIT,
            HEILIG_ABEND,
            ERSTER_WEIHNACHTS_FEIERTAG,
            ZWEITER_WEIHNACHTS_FEIERTAG,
            SYLVESTER
    };

    public static boolean isBankDay(LocalDate datum) {
        return !isWeekend(datum) && !isBankHoliday(datum);
    }

    public static LocalDate nextBankDate(LocalDate datum) {
        LocalDate dateTime = datum;

        while (!isBankDay(dateTime)) {
            dateTime = dateTime.plusDays(1);
        }

        return dateTime;
    }

    public static LocalDate previousBankData(LocalDate datum) {
        LocalDate dateTime = datum;

        while (!isBankDay(dateTime)) {
            dateTime = dateTime.minusDays(1);
        }

        return dateTime;
    }

    public static List<LocalDate> bankDaysInMonth(LocalDate datum) {
        return bankDays(datum, datum.lengthOfMonth());
    }

    public static List<LocalDate> bankDaysLeft(LocalDate datum) {
        return bankDays(datum, datum.getDayOfMonth());
    }

    private static List<LocalDate> bankDays(LocalDate datum, int anzahlTage) {
        return bankDays(datum.getYear(), datum.getMonthValue(), anzahlTage);
    }

    private static List<LocalDate> bankDays(int jahr, int monat, int anzahlTage) {
        List<LocalDate> dateList = new ArrayList<>();

        for (int i = 1; i <= anzahlTage; i++) {
            LocalDate date = LocalDate.of(jahr, monat, i);
            if (isBankDay(date))
                dateList.add(date);
        }

        return dateList;
    }

    public static boolean isBankHoliday(LocalDate tag) {
        return Arrays.stream(FEIERTAGE).anyMatch(f -> f.getDayForYear(tag.getYear()).isEqual(tag));
    }

    private static boolean isWeekend(LocalDate tag) {
        return tag.getDayOfWeek().getValue() > DayOfWeek.FRIDAY.getValue();
    }


}
