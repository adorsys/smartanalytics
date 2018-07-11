package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class PeriodCalculator {

    public static List<BookingPeriod> createBookingPeriods(Optional<Map.Entry<BookingGroup, List<WrappedBooking>>> salaryWageGroupOptional,
                                                           LocalDate firstBookingDate, boolean salaryWagePeriods) {
        List<LocalDate> bookingDates;

        //booking dates past and future based on salary/wage bookings
        if (salaryWageGroupOptional.isPresent() && salaryWagePeriods) {
            bookingDates = calcBookingDates(salaryWageGroupOptional.get().getKey(), salaryWageGroupOptional.get().getValue());
        } else {
            //booking dates based on first available booking beginning at first of month
            bookingDates = new ArrayList<>();
            while (firstBookingDate.isBefore(LocalDate.now())) {
                bookingDates.add(firstBookingDate.withDayOfMonth(1));
                firstBookingDate = firstBookingDate.plusMonths(1);
            }

            for (int i = 0; i < 11; i++) {
                LocalDate previousMonth = bookingDates.get(bookingDates.size() - 1);
                bookingDates.add(previousMonth.plusMonths(1));
            }
        }

        return PeriodCalculator.createBookingPeriods(bookingDates);
    }

    public static List<WrappedBooking> filterPeriodBookings(List<WrappedBooking> bookings, BookingPeriod period) {
        return bookings.stream()
            .filter(wrappedBooking -> dateInPeriod(wrappedBooking.getExecutionDate(), period))
            .collect(Collectors.toList());
    }

    public static List<BookingPeriod> createGroupPeriods(List<BookingPeriod> bookingPeriods, List<WrappedBooking> bookings) {
        return bookingPeriods.stream()
            .map(period -> {
                List<WrappedBooking> periodBookings = filterPeriodBookings(bookings, period);

                BigDecimal periodAmount = periodBookings.stream()
                    .map(WrappedBooking::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BookingPeriod groupPeriod = (BookingPeriod) period.clone();
                groupPeriod.setAmount(periodAmount);
                groupPeriod.setBookings(periodBookings
                    .stream()
                    .map(wrappedBooking -> {
                        ExecutedBooking executedBooking = new ExecutedBooking();
                        executedBooking.setBookingId(wrappedBooking.getBooking().getBookingId());
                        executedBooking.setExecutionDate(wrappedBooking.getBooking().getExecutionDate());
                        executedBooking.setExecuted(true);
                        return executedBooking;
                    })
                    .collect(Collectors.toList()));
                return groupPeriod;

            })
            .filter(period -> period.getBookings().size() > 0)
            .collect(Collectors.toList());
    }

    public static List<BookingPeriod> createBookingPeriods(List<LocalDate> bookingDates) {
        List<BookingPeriod> bookingPeriods = new ArrayList<>();
        for (int i = 0; i < bookingDates.size() - 1; i++) {
            BookingPeriod period = new BookingPeriod();
            period.setStart(bookingDates.get(i));
            period.setEnd(bookingDates.get(i + 1).minusDays(1));

            if (period != null) {
                bookingPeriods.add(period);
            }
        }
        return bookingPeriods;
    }

    public static List<LocalDate> calcBookingDates(BookingGroup bookingGroup, List<WrappedBooking> bookings) {
        LocalDate start = bookings.stream()
            .max(Comparator.comparing(WrappedBooking::getExecutionDate))
            .map(wrappedBooking -> wrappedBooking.getExecutionDate().plusDays(1))
            .orElse(LocalDate.now());
        LocalDate end = YearMonth.from(start.plusMonths(11)).atEndOfMonth();

        List<LocalDate> bookingDates = new ArrayList<>();

        //booking dates past
        bookingDates.addAll(bookings.stream().map(WrappedBooking::getExecutionDate).collect(Collectors.toList()));

        if (!bookingGroup.isCancelled()) {
            //booking dates forecast
            bookingDates.addAll(BookingDateCalculator.calcBookingDates(bookingGroup, bookings, start, end, start));
        }
        
        return bookingDates;
    }

    public static void evalBookingPeriods(Map<BookingGroup, List<WrappedBooking>> groupsMap, List<BookingPeriod> bookingPeriods) {
        groupsMap.entrySet()
            .stream()
            .forEach(bookingGroupListEntry -> {
                List<LocalDate> groupBookingDates = calcBookingDates(bookingGroupListEntry.getKey(), bookingGroupListEntry.getValue());

                List<BookingPeriod> groupPeriods = bookingPeriods.stream()
                    .map(period -> (BookingPeriod) period.clone())
                    .collect(Collectors.toList());

                groupPeriods = groupPeriods.stream()
                    .filter(period -> evalBookingPeriod(period, bookingGroupListEntry.getValue(), groupBookingDates))
                    .collect(Collectors.toList());

                if (groupPeriods.size() > 0) {
                    bookingGroupListEntry.getKey().setBookingPeriods(groupPeriods);
                }
            });
    }

    private static boolean evalBookingPeriod(BookingPeriod period, List<WrappedBooking> bookings, List<LocalDate> bookingDates) {
        List<WrappedBooking> periodBookings = filterPeriodBookings(bookings, period);

        //forecast, clone bookings
        if (periodBookings.size() == 0) {
            period.setBookings(new ArrayList<>());

            bookingDates.stream()
                .filter(executionDate -> dateInPeriod(executionDate, period))
                .forEach(localDate -> {
                    ExecutedBooking forecastBooking = new ExecutedBooking();
                    forecastBooking.setBookingId(bookings.get(0).getBooking().getBookingId());
                    forecastBooking.setExecuted(false);
                    forecastBooking.setExecutionDate(localDate);
                    period.getBookings().add(forecastBooking);
                });

        } else {
            //already executed bookings
            period.setBookings(periodBookings.stream().map(wrappedBooking -> {
                ExecutedBooking executedBooking = new ExecutedBooking();
                executedBooking.setBookingId(wrappedBooking.getBooking().getBookingId());
                executedBooking.setExecuted(true);
                executedBooking.setExecutionDate(wrappedBooking.getBooking().getExecutionDate());
                return executedBooking;
            }).collect(Collectors.toList()));

            period.setAmount(periodBookings.stream()
                .map(WrappedBooking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return period.getBookings().size() > 0 ? true : false;
    }

    public static BookingPeriod filterPeriod(List<BookingPeriod> periods, LocalDate referenceDate) {
        return periods.stream()
            .filter(period -> dateInPeriod(referenceDate, period))
            .findAny()
            .get();
    }

    public static boolean dateInPeriod(LocalDate referenceDate, BookingPeriod period) {
        if (referenceDate == null || period == null) {
            return false;
        }

        LocalDate start = period.getStart();
        LocalDate end = period.getEnd();

        return (referenceDate.isEqual(start) || referenceDate.isAfter(start)) &&
            (referenceDate.isEqual(end) || referenceDate.isBefore(end));

    }
}
