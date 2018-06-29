package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.Cycle;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.calendar.AlternativeBankCalender;
import de.adorsys.smartanalytics.calendar.BankCalendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class NextBookingDateCalculator {

    public static LocalDate calcNextBookingDate(List<WrappedBooking> bookings, boolean income, Cycle cycle) {
        if (cycle == null) {
            return null;
        }

        List<LocalDate> lastBookingDates = getLastBookingDates(bookings);
        LocalDate lastBookingDate = lastBookingDates.get(lastBookingDates.size() - 1);
        List<Integer> indizes = createIndizes(lastBookingDates.size());

        if (lastBookingDates.size() == 1) {
            return correctNextBookingDate(cycle.nextBookingDate(lastBookingDate), income);
        }

        int[] bankDays = lastBookingDates.stream().mapToInt(datum -> BankCalendar.bankDaysInMonth(datum).size()).toArray();
        int[] bankDaysLeft = lastBookingDates.stream().mapToInt(datum -> BankCalendar.bankDaysLeft(datum).size()).toArray();

        return calcNextBookingDate(income, lastBookingDates, cycle, lastBookingDate, indizes, bankDays, bankDaysLeft, false);
    }

    private static List<LocalDate> getLastBookingDates(List<WrappedBooking> bookings) {
        List<LocalDate> bookingDates = bookings.stream()
                .map(wrappedBooking -> wrappedBooking.getNextBookingDate() != null ? wrappedBooking.getNextBookingDate() : wrappedBooking.getExecutionDate())
                .sorted()
                .collect(Collectors.toList());

        if (bookingDates.size() > 3) {
            return bookingDates.subList(bookingDates.size() - 3, bookingDates.size());
        }
        return bookingDates;
    }

    private static LocalDate calcNextBookingDate(boolean income, List<LocalDate> bookingDates, Cycle cycle, LocalDate lastBookingDate, List<Integer> indizes, int[] bankDays, int[] bankDaysLeft, boolean calcWithFixHolidays) {
        int bankDaysElapsed = bankDays[0] - bankDaysLeft[0];

        // all elapsed days same
        if (cycle != Cycle.WEEKLY && indizes.stream().allMatch(i -> bankDays[i] - bankDaysLeft[i] == bankDaysElapsed)) {
            return bankDateNextCycleDaysElapsed(lastBookingDate, cycle, bankDaysElapsed);
        }

        // all left days same
        if (cycle != Cycle.WEEKLY && indizes.stream().allMatch(i -> bankDaysLeft[i] == bankDaysLeft[0])) {
            return bankDateNextCycleDaysLeft(lastBookingDate, cycle, bankDaysLeft[0]);
        }

        //booking dates not same
        if (calcWithFixHolidays) {
            return nextBookingDateWithHolidays(bookingDates, cycle, lastBookingDate, income);
        }

        int[] nNeu = bookingDates.stream().mapToInt(datum -> AlternativeBankCalender.bankDaysInMonth(datum)).toArray();
        int[] mNeu = bookingDates.stream().mapToInt(datum -> AlternativeBankCalender.bankDaysLeft(datum)).toArray();

        return calcNextBookingDate(income, bookingDates, cycle, lastBookingDate, indizes, nNeu, mNeu, true);
    }

    private static LocalDate bankDateNextCycleDaysElapsed(LocalDate bookingDate, Cycle cycle, int bankDaysElapsed) {
        List<LocalDate> bankDaysNextCycle = bankDaysNextCycle(bookingDate, cycle);
        int bankDaysLeft = bankDaysNextCycle.size() - bankDaysElapsed;

        if (bankDaysLeft < 1) {
            return bankDaysNextCycle.get(0);
        }
        return bankDaysNextCycle.get(bankDaysLeft - 1);
    }

    private static LocalDate bankDateNextCycleDaysLeft(LocalDate letztesDatum, Cycle intervall, int bankDaysLeft) {
        List<LocalDate> bankDaysNextCycle = bankDaysNextCycle(letztesDatum, intervall);

        if (bankDaysLeft > bankDaysNextCycle.size()) {
            return bankDaysNextCycle.get(bankDaysNextCycle.size() - 1);
        }
        if (bankDaysLeft < 1) {
            return bankDaysNextCycle.get(0);
        }

        return bankDaysNextCycle.get(bankDaysLeft - 1);
    }

    private static List<LocalDate> bankDaysNextCycle(LocalDate datum, Cycle intervall) {
        LocalDate nextBookingDate = intervall.nextBookingDate(datum);
        return nextBookingDate == null ? Collections.emptyList() : BankCalendar.bankDaysInMonth(nextBookingDate);
    }

    private static LocalDate nextBookingDateWithHolidays(List<LocalDate> bookingDates, Cycle cycle, LocalDate lastBookingDaate, boolean income) {
        LocalDate nexBookingDate;

        if (cycle == Cycle.MONTHLY) {
            nexBookingDate = calcNextDateMonthCycle(bookingDates, lastBookingDaate);
        } else {
            nexBookingDate = cycle.nextBookingDate(lastBookingDaate);
        }

        if (nexBookingDate == null) {
            return null;
        }

        if (nexBookingDate.isBefore(LocalDate.now())) {
            nexBookingDate = LocalDate.now();
        }

        if (BankCalendar.isBankDay(nexBookingDate)) {
            return nexBookingDate;
        }

        return correctNextBookingDate(nexBookingDate, income);
    }

    private static LocalDate correctNextBookingDate(LocalDate geplant, boolean income) {
        if (income) {
            return BankCalendar.previousBankData(geplant);
        }
        return BankCalendar.nextBankDate(geplant);
    }

    private static LocalDate calcNextDateMonthCycle(List<LocalDate> bookingDates, LocalDate lastBookingDate) {
        boolean monthBoundary = false;
        Integer nextBookingDay = null;
        for (int i = 0; i < bookingDates.size() - 1; i++) {
            List<LocalDate> datesInCycle = bookingDates.subList(i, i + 2);
            BookingPeriodCalculator bookingPeriodCalculator = new BookingPeriodCalculator(datesInCycle.get(0), datesInCycle.get(1));
            monthBoundary = bookingPeriodCalculator.isSecondDateNotNexMonth();

            if (monthBoundary) {
                nextBookingDay = bookingPeriodCalculator.nextDay();
            }
        }

        List<Integer> bookingDays = bookingDates.stream().map(LocalDate::getDayOfMonth).collect(Collectors.toList());
        if (!monthBoundary) {
            Map<Integer, Long> bookingDaysCountMap = bookingDays.stream()
                    .collect(Collectors.groupingBy(day -> day, Collectors.counting()));

            // find most frequently booking day
            OptionalInt mostFrequentyDay = bookingDaysCountMap.entrySet().stream()
                    .filter(p -> p.getValue() > 1)
                    .mapToInt(Map.Entry::getKey)
                    .findFirst();

            if (mostFrequentyDay.isPresent())
                nextBookingDay = mostFrequentyDay.getAsInt();
        }

        if (nextBookingDay == null) {
            nextBookingDay = Collections.min(bookingDays);
        }

        LocalDate nextMonth = lastBookingDate.plusMonths(1);
        int lastDayInMonth = YearMonth.from(nextMonth).lengthOfMonth();
        if (lastDayInMonth < nextBookingDay) {
            nextBookingDay = lastDayInMonth;
        }

        LocalDate nextBookingDateNextMonth = nextMonth.withDayOfMonth(nextBookingDay);
        if (ChronoUnit.DAYS.between(nextMonth, nextBookingDateNextMonth) > 15) {
            return nextBookingDateNextMonth.minusMonths(1);
        } else {
            return nextBookingDateNextMonth;
        }
    }

    private static List<Integer> createIndizes(int anzahl) {
        int i = 0;
        List<Integer> indizes = new ArrayList<>();
        while (i < anzahl) {
            indizes.add(i);
            i++;
        }
        return indizes;
    }
}
