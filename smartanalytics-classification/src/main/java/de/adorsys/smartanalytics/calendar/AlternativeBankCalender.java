package de.adorsys.smartanalytics.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

import static de.adorsys.smartanalytics.calendar.BankCalendar.*;

public abstract class AlternativeBankCalender {

    private static final Holiday HEILIG_DREI_KOENIG = new SimpleHoliday(6, 1);
    private static final Holiday MARIAHIMMELFAHRT = new SimpleHoliday(15, 8);
    private static final Holiday REFORMATIONSTAG = new SimpleHoliday(31, 10);
    private static final Holiday ALLERHEILIGEN = new SimpleHoliday(1, 11);
    private static final Holiday BUSSUNDBETTAG = new PrayerRepentanceHoliday();
    private static final Holiday FRONLEICHNAM = new EasterHoliday(60);

    private static final Holiday[] HOLIDAYS = new Holiday[]{
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
            SYLVESTER,
            HEILIG_DREI_KOENIG,
            MARIAHIMMELFAHRT,
            REFORMATIONSTAG,
            ALLERHEILIGEN,
            BUSSUNDBETTAG,
            FRONLEICHNAM};

    private AlternativeBankCalender() {
    }

    public static boolean isBankDay(LocalDate datum) {
        return !isWeekend(datum) && !isBankHoliday(datum);
    }

    public static int bankDaysInMonth(LocalDate datum) {
        return bankDays(datum, datum.lengthOfMonth());
    }

    public static int bankDaysLeft(LocalDate datum) {
        return bankDays(datum, datum.getDayOfMonth());
    }

    private static int bankDays(LocalDate datum, int anzahlTage) {
        return bankDays(datum.getYear(), datum.getMonthValue(), anzahlTage);
    }

    private static int bankDays(int jahr, int monat, int days) {
        int count = 0;

        for (int i = 1; i <= days; i++) {
            if (isBankDay(LocalDate.of(jahr, monat, i)))
                count++;
        }

        return count;
    }

    public static boolean isBankHoliday(LocalDate tag) {
        return Arrays.stream(HOLIDAYS).anyMatch(f -> f.getDayForYear(tag.getYear()).isEqual(tag));
    }

    private static boolean isWeekend(LocalDate tag) {
        return tag.getDayOfWeek().getValue() > DayOfWeek.FRIDAY.getValue();
    }

}
