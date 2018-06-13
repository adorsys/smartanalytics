package de.adorsys.smartanalytics.calendar;

import java.time.LocalDate;

public class EasterHoliday implements Holiday {

    private int offset;

    public EasterHoliday(int offset) {
        this.offset = offset;
    }

    private static LocalDate calcEasterForYear(int year) {
        int a = year % 19;
        int b = year % 4;
        int c = year % 7;
        int m = ((8 * (year / 100) + 13) / 25) - 2;
        int s = (year / 100) - (year / 400) - 2;
        int soundmogleichung = (15 + s - m) % 30;
        int sonnengleichung = (6 + s) % 7;
        int mondentfernung = (soundmogleichung + 19 * a) % 30;

        int x;
        if (mondentfernung == 29)
            x = 28;
        else if (mondentfernung == 28 && a >= 11)
            x = 27;
        else
            x = mondentfernung;

        int e = (2 * b + 4 * c + 6 * x + sonnengleichung) % 7;

        LocalDate date = LocalDate.of(year, 3, 21);
        return date.plusDays(x + e + 1);
    }

    @Override
    public LocalDate getDayForYear(int year) {
        LocalDate ostern = calcEasterForYear(year);
        return ostern.plusDays(offset);
    }
}
