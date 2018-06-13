package de.adorsys.smartanalytics;

import de.adorsys.smartanalytics.api.*;
import de.adorsys.smartanalytics.calculator.AmountCalculator;
import de.adorsys.smartanalytics.calculator.BookingDateCalculator;
import de.adorsys.smartanalytics.calculator.CycleCalculator;
import de.adorsys.smartanalytics.contract.ContractValidator;
import de.adorsys.smartanalytics.group.GroupBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ClassificationService {

    public List<BookingGroup> group(List<WrappedBooking> extendedBooking, List<GroupBuilder> groupBuilderList,
                                    List<Matcher> recurrentWhiteList, List<Matcher> contractBlackList) {
        Map<BookingGroup, List<WrappedBooking>> groupsMap = new HashMap<>();

        extendedBooking.forEach(wrappedBooking -> {
            groupBuilderList.stream()
                    .filter(groupBuilder -> groupBuilder.groupShouldBeCreated(wrappedBooking))
                    .findFirst()
                    .ifPresent(groupBuilder -> {
                        BookingGroup bookingGroup = groupBuilder.createGroup(wrappedBooking);
                        if (bookingGroup != null) {
                            List<WrappedBooking> bookings = groupsMap.get(bookingGroup);
                            if (bookings == null) {
                                bookings = new ArrayList<>();
                                groupsMap.put(bookingGroup, bookings);
                            }
                            bookings.add(wrappedBooking);
                        }
                    });
        });

        List<BookingGroup> groups = extractValidGroups(recurrentWhiteList, contractBlackList, groupsMap);
        handleOtherBookings(groupsMap, groups);

        return groups;
    }

    private List<BookingGroup> extractValidGroups(List<Matcher> recurrentWhiteList, List<Matcher> contractBlackList, Map<BookingGroup, List<WrappedBooking>> groupsMap) {
        List<BookingGroup> groups = new ArrayList<>();

        Iterator<Map.Entry<BookingGroup, List<WrappedBooking>>> iterator = groupsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BookingGroup, List<WrappedBooking>> next = iterator.next();
            if (isValidBookingGroup(next.getKey(), next.getValue(), recurrentWhiteList)) {
                iterator.remove();
                groups.add(eval(next.getKey(), next.getValue(), contractBlackList));
            }
        }

        return groups;
    }

    private void handleOtherBookings(Map<BookingGroup, List<WrappedBooking>> groupsMap, List<BookingGroup> groups) {
        List<WrappedBooking> otherBookings = groupsMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        BookingGroup otherIncomeGroup = handleOtherIncomeBookings(otherBookings);
        if (otherIncomeGroup.getBookingPeriods() != null && otherIncomeGroup.getBookingPeriods().size() > 0) {
            groups.add(otherIncomeGroup);
        }

        BookingGroup otherExpensesGroup = handleOtherExpenseBookings(otherBookings);
        if (otherExpensesGroup.getBookingPeriods() != null && otherExpensesGroup.getBookingPeriods().size() > 0) {
            groups.add(otherExpensesGroup);
        }
    }

    private BookingGroup handleOtherExpenseBookings(List<WrappedBooking> otherBookings) {
        BookingGroup otherExpensesGroup = new BookingGroup(null, null, Group.Type.OTHER_EXPENSES.toString(), Group.Type.OTHER_EXPENSES);
        List<WrappedBooking> otherExpensesBookings = otherBookings
                .stream()
                .filter(wrappedBooking -> wrappedBooking.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());

        if (!otherExpensesBookings.isEmpty()) {
            eval(otherExpensesGroup, otherExpensesBookings, null);
        }
        return otherExpensesGroup;
    }

    private BookingGroup handleOtherIncomeBookings(List<WrappedBooking> otherBookings) {
        BookingGroup otherIncomeGroup = new BookingGroup(null, null, Group.Type.OTHER_INCOME.toString(), Group.Type.OTHER_INCOME);
        List<WrappedBooking> otherIncomeBookings = otherBookings
                .stream()
                .filter(wrappedBooking -> wrappedBooking.getAmount().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());

        if (!otherIncomeBookings.isEmpty()) {
            eval(otherIncomeGroup, otherIncomeBookings, null);
        }
        return otherIncomeGroup;
    }

    private BookingGroup eval(BookingGroup bookingGroup, List<WrappedBooking> bookings, List<Matcher> contractBlackList) {
        if (bookings.size() == 0) {
            return bookingGroup;
        }

        BigDecimal amount;
        LocalDate referenceDate = LocalDate.now();

        if (bookingGroup.isRecurrent()) {
            amount = AmountCalculator.calcAmount(bookings);

            bookingGroup.setCycle(CycleCalculator.fromDates(bookings.stream()
                    .map(WrappedBooking::getExecutionDate)
                    .collect(Collectors.toList())));
            bookingGroup.setContract(ContractValidator.isContract(bookingGroup, bookings, contractBlackList) && bookingGroup.isEffective(referenceDate, bookings));
            bookingGroup.setMainCategory(bookings.get(0).getMainCategory());
            bookingGroup.setSubCategory(bookings.get(0).getSubCategory());
            bookingGroup.setSpecification(bookings.get(0).getSpecification());
            bookingGroup.setHomepage(bookings.get(0).getHomepage());
            bookingGroup.setHotline(bookings.get(0).getHotline());
            bookingGroup.setEmail(bookings.get(0).getEmail());
            bookingGroup.setLogo(bookings.get(0).getLogo());
            bookingGroup.setMandatreference(bookings.get(0).getMandateReference());
            if (StringUtils.isNotBlank(bookings.get(0).getOtherAccount())) {
                bookingGroup.setOtherAccount(bookings.get(0).getOtherAccount());
            } else {
                bookingGroup.setOtherAccount(bookings.get(0).getReferenceName());
            }
        } else {
            LocalDate lastMonth = referenceDate.minusMonths(1);
            BigDecimal amountLastMonth = new BigDecimal(bookings.stream()
                    .filter(booking -> booking.getExecutionDate().getMonthValue() == lastMonth.getMonthValue() &&
                            booking.getExecutionDate().getYear() == lastMonth.getYear())
                    .mapToInt(value -> value.getAmount().intValue())
                    .sum());

            LocalDate secondLastMonth = lastMonth.minusMonths(1);
            BigDecimal amountSecondLastMonth = new BigDecimal(bookings.stream()
                    .filter(extendedBooking -> extendedBooking.getExecutionDate().getMonthValue() == secondLastMonth.getMonthValue() &&
                            extendedBooking.getExecutionDate().getYear() == secondLastMonth.getYear())
                    .mapToInt(value -> value.getAmount().intValue())
                    .sum());

            if (amountLastMonth.compareTo(BigDecimal.ZERO) != 0 && amountSecondLastMonth.compareTo(BigDecimal.ZERO) != 0) {
                amount = amountLastMonth.add(amountSecondLastMonth).divide(new BigDecimal(2));
            } else if (amountLastMonth.compareTo(BigDecimal.ZERO) != 0) {
                amount = amountLastMonth.divide(new BigDecimal(2));
            } else if (amountSecondLastMonth.compareTo(BigDecimal.ZERO) != 0) {
                amount = amountSecondLastMonth.divide(new BigDecimal(2));
            } else {
                amount = BigDecimal.ZERO;
            }
        }

        bookingGroup.setAmount(amount);
        evalBookingPeriods(bookingGroup, bookings);

        return bookingGroup;
    }

    private void evalBookingPeriods(BookingGroup bookingGroup, List<WrappedBooking> bookings) {
        bookingGroup.setBookingPeriods(new ArrayList<>());

        evalPastBookingPeriods(bookingGroup, bookings);
        evalForecastBookingPeriods(bookingGroup, bookings);

    }

    private void evalPastBookingPeriods(BookingGroup bookingGroup, List<WrappedBooking> bookings) {
        LocalDate start = LocalDate.now().withDayOfMonth(1).minusMonths(12);
        LocalDate end;

        for (int i = 0; i < 12; i++) {
            start = start.plusMonths(1);
            end = YearMonth.from(start).atEndOfMonth();
            final Month month = start.getMonth();
            final int year = start.getYear();

            List<WrappedBooking> periodBookings = bookings.stream()
                    .filter(wrappedBooking -> wrappedBooking.getExecutionDate().getMonth() == month &&
                            wrappedBooking.getExecutionDate().getYear() == year)
                    .collect(Collectors.toList());

            if (periodBookings.size() > 0) {
                BigDecimal amount = periodBookings.stream()
                        .map(WrappedBooking::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (amount.compareTo(BigDecimal.ZERO) != 0) {
                    BookingPeriod bookingPeriod = new BookingPeriod();
                    bookingPeriod.setBookings(periodBookings.stream()
                            .map(wrappedBooking -> {
                                ExecutedBooking executedBooking = new ExecutedBooking();
                                executedBooking.setBookingId(wrappedBooking.getBooking().getBookingId());
                                executedBooking.setExecutionDate(wrappedBooking.getBooking().getExecutionDate());
                                executedBooking.setExecuted(true);
                                return executedBooking;
                            })
                            .collect(Collectors.toList()));
                    bookingPeriod.setAmount(amount);
                    bookingPeriod.setStart(start);
                    bookingPeriod.setEnd(end);
                    bookingGroup.getBookingPeriods().add(bookingPeriod);
                }
            }
        }
    }

    private void evalForecastBookingPeriods(BookingGroup bookingGroup, List<WrappedBooking> bookings) {
        if (bookingGroup.isRecurrent() && bookingGroup.isEffective(LocalDate.now(), bookings)) {
            LocalDate start = LocalDate.now();
            LocalDate end = YearMonth.from(start.plusMonths(11)).atEndOfMonth();

            List<LocalDate> bookingDates = BookingDateCalculator.calcBookingDates(bookingGroup, bookings, start, end, start);
            if (bookingDates.size() > 0) {
                for (int i = 0; i < 12; i++) {
                    final Month referenceMonth = start.getMonth();
                    final int referenceYear = start.getYear();

                    List<LocalDate> periodBookingDates = bookingDates.stream()
                            .filter(bookingDate -> bookingDate.getMonth() == referenceMonth && bookingDate.getYear() == referenceYear)
                            .collect(Collectors.toList());

                    if (periodBookingDates.size() > 0) {
                        List<ExecutedBooking> simpleBookings = periodBookingDates.stream()
                                .map(localDate -> {
                                    ExecutedBooking executedBooking = new ExecutedBooking();
                                    executedBooking.setBookingId(bookings.get(0).getBooking().getBookingId());
                                    executedBooking.setExecuted(false);
                                    executedBooking.setExecutionDate(localDate);
                                    return executedBooking;
                                })
                                .collect(Collectors.toList());


                        BookingPeriod bookingPeriod;
                        Optional<BookingPeriod> bookingPeriodOptional = getBookingPeriod(bookingGroup.getBookingPeriods(), start);
                        if (bookingPeriodOptional.isPresent()) {
                            bookingPeriod = bookingPeriodOptional.get();
                            bookingPeriod.getBookings().addAll(simpleBookings);
                        } else {
                            bookingPeriod = new BookingPeriod();
                            bookingPeriod.setBookings(simpleBookings);
                            bookingPeriod.setStart(start.withDayOfMonth(1));
                            bookingPeriod.setEnd(YearMonth.from(start).atEndOfMonth());
                        }
                        bookingGroup.getBookingPeriods().add(bookingPeriod);
                    }
                    start = start.plusMonths(1);
                }
            }
        }
    }

    private Optional<BookingPeriod> getBookingPeriod(List<BookingPeriod> bookingPeriods, LocalDate date) {
        return bookingPeriods.stream()
                .filter(bookingPeriod -> bookingPeriod.getStart().getMonth() == date.getMonth() && bookingPeriod.getStart().getYear() == date.getYear())
                .findFirst();
    }


    private boolean isValidBookingGroup(BookingGroup bookingGroup, List<WrappedBooking> bookings, List<Matcher> whitelist) {
        if (bookingGroup.getGroupType() == Group.Type.OTHER_INCOME || bookingGroup.getGroupType() == Group.Type.OTHER_EXPENSES) {
            return false;
        }

        if (bookingGroup.getGroupType() == Group.Type.CUSTOM || isWhitelisted(bookings, whitelist)) {
            return true;
        }

        if (bookings.size() > 1) {
            return CycleCalculator.fromDates(bookings.stream()
                    .map(WrappedBooking::getExecutionDate)
                    .collect(Collectors.toList())) != null;
        }

        return false;
    }

    private boolean isWhitelisted(List<WrappedBooking> bookings, List<Matcher> whitelist) {
        if (whitelist == null) {
            return false;
        }
        for (Matcher matcher : whitelist) {
            for (WrappedBooking booking : bookings) {
                if (matcher.match(booking)) {
                    return true;
                }
            }
        }
        return false;
    }


}
