package de.adorsys.smartanalytics.calculator;

import java.time.LocalDate;

class BookingPeriodCalculator {


    private boolean secondDateNotNexMonth;
    private Integer nextDay = null;

    public BookingPeriodCalculator(LocalDate date1, LocalDate date2) {

        int monthsDiff = date2.getMonthValue() - date1.getMonthValue();

        if (date2.getYear() > date1.getYear()) {
            monthsDiff += 12;
        }

        this.secondDateNotNexMonth = monthsDiff != 1;

        if (monthsDiff == 0) {
            this.nextDay = date2.getDayOfMonth();
        }
        if (monthsDiff == 2) {
            this.nextDay = date1.getDayOfMonth();
        }
    }

    public Integer nextDay() {
        return nextDay;
    }

    boolean isSecondDateNotNexMonth() {
        return secondDateNotNexMonth;
    }
}
