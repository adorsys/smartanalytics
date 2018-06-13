package de.adorsys.smartanalytics.calculator;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.Cycle;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDateCalculator {

    public static List<LocalDate> calcBookingDates(BookingGroup bookingGroup, List<WrappedBooking> bookings, LocalDate firstDate,
                                                   LocalDate lastDate, LocalDate referenceDate) {
        if (bookings.isEmpty() || bookingGroup.getCycle() == null) {
            return Collections.emptyList();
        }

        List<WrappedBooking> previousBookings = bookings.stream()
                .filter(booking -> booking.getExecutionDate().isBefore(firstDate))
                .collect(Collectors.toList());
        if (previousBookings.isEmpty()) {
            return new ArrayList<>();
        }

        List<LocalDate> result = new ArrayList<>();
        result.add(NextBookingDateCalculator.calcNextBookingDate(previousBookings, bookingGroup.isIncome(), bookingGroup.getCycle()));

        List<WrappedBooking> bookingsWithNextBooking = new ArrayList<>(previousBookings);
        WrappedBooking clone = previousBookings.get(0).clone();
        clone.setNextBookingDate(result.get(0));
        bookingsWithNextBooking.add(clone);

        //fill with all possible booking dates
        result.addAll(calcNextBookingDates(bookingsWithNextBooking, bookingGroup.isIncome(), bookingGroup.getCycle(), lastDate));

        filterInactiveBookings(result, bookingGroup, bookings, referenceDate);

        return result;
    }

    private static List<LocalDate> calcNextBookingDates(List<WrappedBooking> bookingsWithNextBookings,
                                                        boolean income, Cycle cycle, LocalDate endDate) {
        List<LocalDate> result = new ArrayList<>();

        LocalDate nextBookingDate = NextBookingDateCalculator.calcNextBookingDate(bookingsWithNextBookings, income, cycle);
        while (nextBookingDate.isBefore(endDate)) {
            result.add(nextBookingDate);

            WrappedBooking clone = bookingsWithNextBookings.get(0).clone();
            clone.setNextBookingDate(nextBookingDate);
            bookingsWithNextBookings.add(clone);

            nextBookingDate = NextBookingDateCalculator.calcNextBookingDate(bookingsWithNextBookings, income, cycle);
        }
        return result;
    }

    private static void filterInactiveBookings(List<LocalDate> result, BookingGroup bookingGroup, List<WrappedBooking> bookings, LocalDate executionDate) {
        if (result.isEmpty()) {
            return;
        }
        List<LocalDate> bookingDates = bookings.stream()
                .map(WrappedBooking::getExecutionDate)
                .collect(Collectors.toList());
        LocalDate lastBookingDate = LocalDate.ofEpochDay(0);
        for (WrappedBooking booking : bookings) {
            if (booking.getExecutionDate().isAfter(lastBookingDate)) {
                lastBookingDate = booking.getExecutionDate();
            }
        }

        if (!bookingGroup.getCycle().isValid(lastBookingDate, executionDate)) {
            for (Iterator<LocalDate> it = result.iterator(); it.hasNext(); ) {
                LocalDate nextBookingDate = it.next();
                if (nextBookingDate.isAfter(lastBookingDate)) {
                    it.remove();
                }
            }
        }

        result.removeIf(plandatum -> !bookingGroup.getCycle().isReferenceDateBeforeBookingDateWithTolerance(plandatum, executionDate)
                && !bookingGroup.getCycle().isOneValidWithinTolerance(plandatum, bookingDates));
    }


}
