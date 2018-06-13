package de.adorsys.smartanalytics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class TestDateHelper {

    private TestDateHelper() {
        // private constructor
    }

    public static LocalDate createDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }
}
