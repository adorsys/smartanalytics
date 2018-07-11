package de.adorsys.smartanalytics;

import de.adorsys.smartanalytics.api.*;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.calculator.AmountCalculator;
import de.adorsys.smartanalytics.calculator.CycleCalculator;
import de.adorsys.smartanalytics.contract.ContractValidator;
import de.adorsys.smartanalytics.group.GroupBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static de.adorsys.smartanalytics.calculator.PeriodCalculator.*;

public class ClassificationService {

    public List<BookingGroup> group(List<WrappedBooking> wrappedBookings, List<GroupBuilder> groupBuilderList,
                                    List<Matcher> recurrentWhiteList, List<Matcher> contractBlackList, boolean salaryWagePeriods) {

        Map<BookingGroup, List<WrappedBooking>> groupsMap = createGroups(wrappedBookings, groupBuilderList);
        Map<BookingGroup, List<WrappedBooking>> validGroups = filterValidGroups(recurrentWhiteList, groupsMap);

        //amount and cycle group calculation for recurrent groups, needed for salary/wage group detection
        validGroups.entrySet()
                .stream()
                .filter(bookingGroupListEntry -> bookingGroupListEntry.getKey().isRecurrent())
                .forEach(bookingGroupListEntry ->
                        evalRecurrentGroup(bookingGroupListEntry.getKey(), bookingGroupListEntry.getValue(), contractBlackList));

        Optional<Map.Entry<BookingGroup, List<WrappedBooking>>> salaryWageGroupOptional = findSalaryWageGroup(validGroups);
        if (salaryWageGroupOptional.isPresent() && salaryWagePeriods) {
            salaryWageGroupOptional.get().getKey().setSalaryWage(true);
        }

        List<BookingPeriod> bookingPeriods = createBookingPeriods(salaryWageGroupOptional, getFirstBookingDate(wrappedBookings), salaryWagePeriods);

        //amount and cycle group calculation for non recurrent groups
        validGroups.entrySet()
                .stream()
                .filter(bookingGroupListEntry -> !bookingGroupListEntry.getKey().isRecurrent())
                .forEach(bookingGroupListEntry ->
                        evalNonRecurrentGroup(bookingGroupListEntry.getKey(), bookingGroupListEntry.getValue(), bookingPeriods));


        evalBookingPeriods(validGroups, bookingPeriods);

        List<BookingGroup> result = new ArrayList<>(validGroups.keySet());
        result.addAll(handleOtherBookings(groupsMap, bookingPeriods));

        return result;
    }

    private LocalDate getFirstBookingDate(List<WrappedBooking> wrappedBookings) {
        return wrappedBookings.stream()
                .min(Comparator.comparing(WrappedBooking::getExecutionDate))
                .map(WrappedBooking::getExecutionDate)
                .get();
    }

    private Map<BookingGroup, List<WrappedBooking>> createGroups(List<WrappedBooking> wrappedBookings, List<GroupBuilder> groupBuilderList) {
        Map<BookingGroup, List<WrappedBooking>> groupsMap = new HashMap<>();

        wrappedBookings.forEach(wrappedBooking -> {
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

        return groupsMap;
    }

    private Map<BookingGroup, List<WrappedBooking>> filterValidGroups(List<Matcher> recurrentWhiteList,
                                                                      Map<BookingGroup, List<WrappedBooking>> groupsMap) {
        Map<BookingGroup, List<WrappedBooking>> validGroups = new HashMap<>();

        Iterator<Map.Entry<BookingGroup, List<WrappedBooking>>> iterator = groupsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BookingGroup, List<WrappedBooking>> next = iterator.next();
            if (isValidBookingGroup(next.getKey(), next.getValue(), recurrentWhiteList)) {
                iterator.remove();
                validGroups.put(next.getKey(), next.getValue());
            }
        }

        return validGroups;
    }

    private List<BookingGroup> handleOtherBookings(Map<BookingGroup, List<WrappedBooking>> groupsMap, List<BookingPeriod> bookingPeriods) {
        List<BookingGroup> groups = new ArrayList<>();

        List<WrappedBooking> otherBookings = groupsMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        BookingGroup otherIncomeGroup = handleOtherIncomeBookings(otherBookings, bookingPeriods);
        if (otherIncomeGroup != null) {
            groups.add(otherIncomeGroup);
        }

        BookingGroup otherExpensesGroup = handleOtherExpenseBookings(otherBookings, bookingPeriods);
        if (otherExpensesGroup != null) {
            groups.add(otherExpensesGroup);
        }

        return groups;
    }

    private BookingGroup handleOtherExpenseBookings(List<WrappedBooking> otherBookings, List<BookingPeriod> bookingPeriods) {
        BookingGroup otherExpensesGroup = new BookingGroup(null, null, Group.Type.OTHER_EXPENSES.toString(), Group.Type.OTHER_EXPENSES);

        List<WrappedBooking> otherExpensesBookings = otherBookings
                .stream()
                .filter(wrappedBooking -> wrappedBooking.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());

        if (!otherExpensesBookings.isEmpty() && evalNonRecurrentGroup(otherExpensesGroup, otherExpensesBookings, bookingPeriods)) {
            List<BookingPeriod> groupPeriods = createGroupPeriods(bookingPeriods, otherExpensesBookings);

            if (groupPeriods.size() > 0) {
                otherExpensesGroup.setBookingPeriods(groupPeriods);
            }

            return otherExpensesGroup;
        }
        return null;
    }

    private BookingGroup handleOtherIncomeBookings(List<WrappedBooking> otherBookings, List<BookingPeriod> bookingPeriods) {
        BookingGroup otherIncomeGroup = new BookingGroup(null, null, Group.Type.OTHER_INCOME.toString(), Group.Type.OTHER_INCOME);

        List<WrappedBooking> otherIncomeBookings = otherBookings
                .stream()
                .filter(wrappedBooking -> wrappedBooking.getAmount().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());

        if (!otherIncomeBookings.isEmpty() && evalNonRecurrentGroup(otherIncomeGroup, otherIncomeBookings, bookingPeriods)) {
            List<BookingPeriod> groupPeriods = createGroupPeriods(bookingPeriods, otherIncomeBookings);

            if (groupPeriods.size() > 0) {
                otherIncomeGroup.setBookingPeriods(groupPeriods);
            }

            return otherIncomeGroup;
        }
        return null;
    }

    private boolean evalNonRecurrentGroup(BookingGroup bookingGroup, List<WrappedBooking> bookings, List<BookingPeriod> bookingPeriods) {
        if (bookings.size() == 0) {
            return false;
        }

        int indexCurrentPeriod = bookingPeriods.indexOf(filterPeriod(bookingPeriods, LocalDate.now()));
        BookingPeriod lastPeriod = indexCurrentPeriod > 0 ? bookingPeriods.get(indexCurrentPeriod - 1) : null;
        BookingPeriod secondLastPeriod = indexCurrentPeriod > 1 ? bookingPeriods.get(indexCurrentPeriod - 2) : null;

        BigDecimal amountLastPeriod = bookings.stream()
                .filter(booking -> dateInPeriod(booking.getExecutionDate(), lastPeriod))
                .map(value -> value.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal amountSecondLastPeriod = bookings.stream()
                .filter(booking -> dateInPeriod(booking.getExecutionDate(), secondLastPeriod))
                .map(value -> value.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal amount;
        if (amountLastPeriod.compareTo(BigDecimal.ZERO) != 0 && amountSecondLastPeriod.compareTo(BigDecimal.ZERO) != 0) {
            amount = amountLastPeriod.add(amountSecondLastPeriod).divide(new BigDecimal(2));
        } else if (amountLastPeriod.compareTo(BigDecimal.ZERO) != 0) {
            amount = amountLastPeriod;
        } else {
            amount = BigDecimal.ZERO;
        }

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        bookingGroup.setAmount(amount);
        return true;
    }

    private boolean evalRecurrentGroup(BookingGroup bookingGroup, List<WrappedBooking> bookings, List<Matcher> contractBlackList) {
        if (bookings.size() == 0) {
            return false;
        }

        BigDecimal amount = AmountCalculator.calcAmount(bookings);
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        bookingGroup.setCycle(CycleCalculator.fromDates(bookings.stream()
                .map(WrappedBooking::getExecutionDate)
                .collect(Collectors.toList())));
        bookingGroup.setContract(ContractValidator.isContract(bookingGroup, bookings, contractBlackList));
        bookingGroup.setCancelled(!bookingGroup.isEffective(LocalDate.now(), bookings));
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
        bookingGroup.setAmount(amount);
        return true;
    }

    private Optional<Map.Entry<BookingGroup, List<WrappedBooking>>> findSalaryWageGroup(Map<BookingGroup, List<WrappedBooking>> groups) {
        return groups.entrySet()
                .stream()
                .filter(salaryWageGroup -> salaryWageGroup.getKey().isSalaryWageGroup())
                .max(Comparator.comparing(o -> o.getKey().getAmount()));

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
