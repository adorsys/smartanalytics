package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    private LocalDate analyticsDate;
    private BigDecimal incomeTotal = new BigDecimal(0);

    private BigDecimal incomeFix = new BigDecimal(0);
    private List<BookingGroup> incomeFixBookings = new ArrayList<>();
    private BigDecimal incomeVariable = new BigDecimal(0);
    private List<BookingGroup> incomeVariableBookings = new ArrayList<>();
    private BigDecimal incomeNext = new BigDecimal(0);
    private List<BookingGroup> incomeNextBookings = new ArrayList<>();

    private BigDecimal expensesTotal = new BigDecimal(0);

    private BigDecimal expensesFix = new BigDecimal(0);
    private List<BookingGroup> expensesFixBookings = new ArrayList<>();
    private BigDecimal expensesVariable = new BigDecimal(0);
    private List<BookingGroup> expensesVariableBookings = new ArrayList<>();
    private BigDecimal expensesNext = new BigDecimal(0);
    private List<BookingGroup> expensesNextBookings = new ArrayList<>();
}
