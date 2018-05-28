package de.adorsys.smartanalytics.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingPeriod {

    private LocalDate start;
    private LocalDate end;
    private BigDecimal amount;
    private List<ExecutedBooking> bookings;
}
