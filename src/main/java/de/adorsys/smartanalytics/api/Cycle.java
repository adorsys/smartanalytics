package de.adorsys.smartanalytics.api;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alexg on 21.08.17.
 */
public enum Cycle {

    WEEKLY(6, 8, 14, 3),
    MONTHLY(27, 35, 40, 5),
    TWO_MONTHLY(54, 70, 78, 5),
    QUARTERLY(85, 100, 110, 7),
    HALF_YEARLY(170, 195, 200, 7),
    YEARLY(355, 375, 380, 10);

    private final int minDays;
    private final int maxDays;
    private final long maxDiff;
    private final int tolerance;

    Cycle(int minDays, int maxDays, long maxDiff, int tolerance) {
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.maxDiff = maxDiff;
        this.tolerance = tolerance;
    }

    public boolean isValid(LocalDate bookingDate, LocalDate referenceDate) {
        long dateDiff = ChronoUnit.DAYS.between(bookingDate, referenceDate);
        return dateDiff < maxDiff;
    }

    public boolean isReferenceDateBeforeBookingDateWithTolerance(LocalDate bookingDate, LocalDate referenceDate) {
        LocalDate dateWithTolerance = bookingDate.plusDays(tolerance);
        return referenceDate.isBefore(dateWithTolerance);
    }

    public boolean isOneValidWithinTolerance(LocalDate bookingDate, List<LocalDate> bookingDates) {
        return bookingDates.stream().anyMatch(datum -> isValidWithinTolerance(bookingDate, datum));
    }

    public boolean isValidWithinTolerance(LocalDate nextBookingDate, LocalDate lastBookingDate) {
        return lastBookingDate.isAfter(nextBookingDate.minusDays(tolerance + 1L))
                && !lastBookingDate.isAfter(nextBookingDate.plusDays(tolerance));
    }

    public boolean isBookingGroupValid(List<WrappedBooking> bookings, LocalDate bookingDate) {
        List<WrappedBooking> withinTolerance = bookings.stream()
                .filter(booking -> booking.getExecutionDate().isAfter(bookingDate.minusDays(maxDiff)))
                .filter(booking -> !booking.getExecutionDate().isAfter(bookingDate))
                .collect(Collectors.toList());
        return !withinTolerance.isEmpty();
    }

    public LocalDate nextBookingDate(LocalDate datum) {
        if (this == WEEKLY) {
            return datum.plusWeeks(1);
        }
        if (this == MONTHLY) {
            return datum.plusMonths(1);
        }
        if (this == TWO_MONTHLY) {
            return datum.plusMonths(2);
        }
        if (this == QUARTERLY) {
            return datum.plusMonths(3);
        }
        if (this == HALF_YEARLY) {
            return datum.plusMonths(6);
        }
        return datum.plusYears(1);
    }

    public int getMinDays() {
        return minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }
}
